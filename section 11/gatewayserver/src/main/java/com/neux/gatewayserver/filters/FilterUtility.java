package com.neux.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
// 存放請求和回應追蹤過濾器共用邏輯
public class FilterUtility {
    public static final String CORRELATION_ID = "neuxbank-correlation-id";

    public String getCorrelationId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(CORRELATION_ID) != null) { // 在請求頭中，我們會檢查是否存在名為 neuxbank-correlation-id 的標頭。
            List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
            return requestHeaderList.stream().findFirst().get(); // 如果存在標頭，我們會傳回該標頭的值。
        } else {
            return null; // 如果不存在，則傳回空值。
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        // 在這個方法中，我們建立一個新的請求標頭，其標頭名稱為 neuxbank-correlation-id，標頭值則來自 RequestTraceFilter。
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}

