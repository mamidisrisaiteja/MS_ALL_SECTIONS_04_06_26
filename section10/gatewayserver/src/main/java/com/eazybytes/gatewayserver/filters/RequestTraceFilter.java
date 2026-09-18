package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



@Component// used for automatic bean detection via class path scanning
//while @Bean is used for manual bean creation insed @Configuration class
//both creates beans in spring ioc containers but difference lies in who creates the object and
//its scope
@Order(1)
public class RequestTraceFilter implements GlobalFilter {
    private static Logger logger= LoggerFactory.getLogger(RequestTraceFilter.class);
    @Autowired
    FilterUtility filterUtility;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders requestHeaders=exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeaders)){
            logger.debug("eazyBank-correlation-id found in Request Trace Filter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        }else{
            String correlationId=generateCorrelationId();
            exchange=filterUtility.setCorrelationId(exchange,correlationId);
            logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}", correlationId);
        }
        return chain.filter(exchange);
    }
    public boolean isCorrelationIdPresent(HttpHeaders requestHeaders){
        if(filterUtility.getCorrelationId(requestHeaders)!=null){
            return true;
        }
        else return false;
    }
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
