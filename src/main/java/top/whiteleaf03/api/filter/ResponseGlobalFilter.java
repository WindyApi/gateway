package top.whiteleaf03.api.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.whiteleaf03.api.util.TimeUtil;

@Slf4j
@Component
public class ResponseGlobalFilter implements GlobalFilter, Ordered {
    private final TimeUtil timeUtil;

    @Autowired
    public ResponseGlobalFilter(TimeUtil timeUtil) {
        this.timeUtil = timeUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        timeUtil.endTiming();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
