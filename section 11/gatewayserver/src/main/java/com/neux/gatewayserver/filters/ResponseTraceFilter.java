package com.neux.gatewayserver.filters;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;

@Configuration
// 此過濾器的目的是將追蹤ID添加到回應中，讓客戶端能夠知道每個請求的對應追蹤ID。
// 我使用了一種不同風格的定義GlobalFilter。
public class ResponseTraceFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Bean // 由於我們需要將這個 GlobalFilter 物件註冊為 bean，因此必須加上 @Bean 註釋
    // 我們也可以透過定義 GlobalFilter 類型的 bean 來建立自訂過濾器。
    public GlobalFilter postGlobalFilter() {
        // 這樣，當所有請求處理完成並準備將回應傳送回客戶端時，ResponseTraceFilter會作為後置過濾器，在回應中加入一個新的標頭。
        return (exchange, chain) -> { // 接收相同的 ServerWebExchange 和相同的 GatewayFilterChain
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // 目的：ResponseTraceFilter 只會在請求被發送到對應的微服務並收到回應後才執行
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders(); // 當我們從相應的微服務收到回應後，系統會嘗試獲取請求標頭。
                String correlationId = filterUtility.getCorrelationId(requestHeaders); // 透過 FilterUtility 中的相關方法，我們可以取得相關ID。
                if(!(exchange.getResponse().getHeaders().containsKey(filterUtility.CORRELATION_ID))) {
                    logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                    exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId); // 這個相關ID值會被傳遞到回應標頭中，使用與請求標頭相同的名稱 neuxbank-correlation-id。
                }
                // logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                // exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId); // 這個相關ID值會被傳遞到回應標頭中，使用與請求標頭相同的名稱 neuxbank-correlation-id。
            }));
        };
    }
}
