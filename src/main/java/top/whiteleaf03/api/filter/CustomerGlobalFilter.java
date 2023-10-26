package top.whiteleaf03.api.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.whiteleaf03.api.dubbo.interfaceinfo.InterfaceInfoDubboService;
import top.whiteleaf03.api.dubbo.user.UserDubboService;
import top.whiteleaf03.api.dubbo.userinterfacerecord.UserInterfaceRecordDubboService;
import top.whiteleaf03.api.modal.dto.InterfaceIdAndStatusDTO;
import top.whiteleaf03.api.modal.dto.InterfaceInvokeRecordDTO;
import top.whiteleaf03.api.modal.dto.UserIdAndInterfaceIdDTO;
import top.whiteleaf03.api.modal.dto.UserIdAndSecretKeyDTO;
import top.whiteleaf03.api.util.ResponseResult;
import top.whiteleaf03.api.util.SignUtil;
import top.whiteleaf03.api.util.TimeUtil;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/**
 * @author WhiteLeaf03
 */
@Slf4j
@Order(-1)
@Component
public class CustomerGlobalFilter implements GlobalFilter {
    @DubboReference
    private UserDubboService userDubboService;
    @DubboReference
    private InterfaceInfoDubboService interfaceInfoDubboService;
    @DubboReference
    private UserInterfaceRecordDubboService userInterfaceRecordDubboService;
    private static final String TIMESTAMP_HEADER = "timestamp";
    private static final String ACCESS_KEY_HEADER = "accessKey";
    private static final String SIGN_HEADER = "sign";
    private static final String PARAMS_HEADER = "params";
    private static final String BODY_HEADER = "body";
    private static final long FIVE_MINUTES = Duration.ofMinutes(5).toMillis();
    private ServerHttpRequest request;
    private ServerHttpResponse response;
    private InterfaceInvokeRecordDTO interfaceInvokeRecordDTO;
    private final TimeUtil timeUtil;

    @Autowired
    public CustomerGlobalFilter(TimeUtil timeUtil) {
        this.timeUtil = timeUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        request = exchange.getRequest();
        response = exchange.getResponse();

        // 通往用户中心，直接放行
        if (request.getPath().toString().startsWith("/platform/api/center")) {
            log.info("请求通过");
            return chain.filter(exchange);
        }

        // 接口中心Actuator，直接放行
        if (request.getPath().toString().startsWith("/platform/api/interface/actuator")) {
            log.info("请求通过");
            return chain.filter(exchange);
        }

        // 打印日志并保存记录
        printLogAndSaveRecord();

        // 获取请求头
        HttpHeaders headers = request.getHeaders();

        // 获取时间戳
        String timestamp = headers.getFirst(TIMESTAMP_HEADER);
        if (StrUtil.isBlank(timestamp)) {
            log.info("请求未通过 原因:未携带时间戳");
            return intercept("请求未通过 原因:未携带时间戳", null);
        }

        // 查询用户的id和secretKey
        String accessKey = headers.getFirst(ACCESS_KEY_HEADER);
        UserIdAndSecretKeyDTO userIdAndSecretKeyDTO = userDubboService.selectUserIdAndSecretKeyByAccessKey(accessKey);
        if (Objects.isNull(userIdAndSecretKeyDTO)) {
            log.info("请求未通过 原因:accessKey错误");
            return intercept("请求未通过 原因:accessKey错误", null);
        }
        Long userId = userIdAndSecretKeyDTO.getUserId();

        // 签名校验
        String requestBody = (request.getMethod() == HttpMethod.GET) ? headers.getFirst(PARAMS_HEADER) : headers.getFirst(BODY_HEADER);
        String validSign = SignUtil.genSign(timestamp, requestBody, userIdAndSecretKeyDTO.getSecretKey());
        log.info("校验签名:{}", validSign);
        if (!StrUtil.equals(headers.getFirst(SIGN_HEADER), validSign)) {
            // 签名校验失败
            response.setStatusCode(HttpStatus.FORBIDDEN);
            log.info("请求未通过 原因:签名校验失败");
            return intercept("请求未通过 原因:签名校验失败", userId);
        }

        // 超时判断
        if (isTimestampExpired(Long.parseLong(timestamp))) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            log.info("请求未通过 原因:请求超时");
            return intercept("请求未通过 原因:请求超时", userId);
        }

        // 判断接口状态
        InterfaceIdAndStatusDTO interfaceIdAndStatusDTO = interfaceInfoDubboService.selectIdAndStatusByUrl(request.getPath().toString());
        if (!interfaceIdAndStatusDTO.getStatus()) {
            // 接口已下线，不允许调用
            log.info("请求未通过 原因:接口已下线");
            return intercept("请求未通过 原因:接口已下线", userId);
        }

        // 判断用户剩余可用次数
        UserIdAndInterfaceIdDTO userIdAndInterfaceIdDTO = new UserIdAndInterfaceIdDTO(userIdAndSecretKeyDTO.getUserId(), interfaceIdAndStatusDTO.getInterfaceId());
        Long leftNum = userInterfaceRecordDubboService.selectLeftNumByUserIdAndInterfaceId(userIdAndInterfaceIdDTO);
        if (Objects.isNull(leftNum) || leftNum <= 0) {
            log.info("请求未通过 原因:剩余调用次数不足");
            return intercept("请求未通过 原因:剩余调用次数不足", userId);
        }
        userInterfaceRecordDubboService.updateLeftNumByUserIdAndInterfaceId(userIdAndInterfaceIdDTO);
        log.info("请求通过");
        setResult(Boolean.TRUE, "请求通过", userId);
        return chain.filter(exchange);
    }

    private boolean isTimestampExpired(long timestamp) {
        return (System.currentTimeMillis() - timestamp > FIVE_MINUTES);
    }

    private void printLogAndSaveRecord() {
        log.info("请求id:{}", request.getId());
        log.info("请求来源:{}", request.getRemoteAddress());
        log.info("请求方式:{}", request.getMethod());
        log.info("请求路径:{}", request.getPath());
        log.info("请求时间戳:{}", request.getHeaders().getFirst("timestamp"));
        Long currentTimestamp = System.currentTimeMillis();
        log.info("接收时间戳:{}", currentTimestamp);

        // 保存记录
        interfaceInvokeRecordDTO = new InterfaceInvokeRecordDTO(request.getId(), request.getRemoteAddress(), request.getMethodValue(), request.getPath(), request.getHeaders().getFirst("timestamp"), currentTimestamp, request.getHeaders(), request.getQueryParams(), request.getBody(), request.getHeaders().getFirst("accessKey"), request.getHeaders().getFirst("sign"));
        interfaceInvokeRecordDTO.setUseTime(System.currentTimeMillis());
        timeUtil.saveRecord(interfaceInvokeRecordDTO);

        if (request.getMethod() == HttpMethod.GET) {
            // GET请求
            log.info("请求头参数:{}", request.getHeaders().getFirst("params"));
            log.info("实际请求参数:{}", request.getQueryParams());
        } else {
            // 非GET请求
            log.info("请求体:{}", request.getHeaders().getFirst("body"));
            log.info("实际请求体:{}", request.getBody());
        }
        log.info("请求accessKey:{}", request.getHeaders().getFirst("accessKey"));
        log.info("请求签名:{}", request.getHeaders().getFirst("sign"));
    }

    private Mono<Void> intercept(String msg, Long userId) {
        String responseResult = JSONUtil.toJsonStr(ResponseResult.error(msg));
        byte[] bytes = responseResult.getBytes(StandardCharsets.UTF_8);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=" + StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        String errorMsg = (msg.split(":"))[1];
        setResult(Boolean.FALSE, errorMsg, userId);
        timeUtil.endTiming();
        return response.writeWith(Mono.just(buffer));
    }

    private void setResult(Boolean accept, String msg, Long userId) {
        interfaceInvokeRecordDTO.setAccept(accept);
        interfaceInvokeRecordDTO.setMsg(msg);
        interfaceInvokeRecordDTO.setUserId(userId);
    }
}
