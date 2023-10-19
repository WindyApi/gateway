package top.whiteleaf03.api.modal.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;

/**
 * @author WhiteLeaf03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceInvokeRecordDTO {
    /**
     * 请求id
     */
    private String id;

    /**
     * 请求来源
     */
    private String remoteAddress;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 携带时间戳
     */
    private Long timestamp;

    /**
     * 当前时间戳
     */
    private Long currentTimestamp;

    /**
     * 请求头
     */
    private org.bson.Document requestHeader;

    /**
     * 请求参数
     */
    private org.bson.Document requestParams;

    /**
     * 请求体
     */
    private org.bson.Document body;

    /**
     * 用户ak
     */
    private String accessKey;

    /**
     * 用户签名
     */
    private String sign;

    /**
     * 请求耗时
     */
    private Long useTime;

    /**
     * 是否通过
     */
    private Boolean accept;

    /**
     * 失败原因
     */
    private String msg;

    public InterfaceInvokeRecordDTO(String id, InetSocketAddress remoteAddress, String method, RequestPath requestPath, String timestamp, Long currentTimestamp, HttpHeaders requestHeader, MultiValueMap<String, String> requestParams, Flux body, String accessKey, String sign) {
        this.id = id;
        this.remoteAddress = ObjectUtil.isNotNull(remoteAddress) ? remoteAddress.getHostString() : "";
        this.method = method;
        this.requestPath = requestPath.toString();
        this.timestamp = StrUtil.isNotBlank(timestamp) ? Long.parseLong(timestamp) : Long.MIN_VALUE;
        this.currentTimestamp = currentTimestamp;
        String headerJson = JSON.toJSONString(requestHeader);
        this.requestHeader = org.bson.Document.parse(headerJson);
        String paramsJson = JSON.toJSONString(requestParams);
        this.requestParams = org.bson.Document.parse(paramsJson);
        String bodyJson = JSON.toJSONString(body);
        this.body = org.bson.Document.parse(bodyJson);
        this.accessKey = accessKey;
        this.sign = sign;
    }
}
