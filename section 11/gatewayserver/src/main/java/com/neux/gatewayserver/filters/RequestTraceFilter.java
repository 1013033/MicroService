package com.neux.gatewayserver.filters;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;

/*
當您需要定義多個過濾器時，可以使用 @Order 註解來指定它們的執行順序。
您可以為每個過濾器設定數值如 1、2、3、4 來決定其優先順序。
系統會按照這些指定的數值順序執行所有過濾器。
例如，設定 @Order(1) 的過濾器會確保它最先在APIGateway伺服器中執行。
* */
@Order(1)
@Component // 透過@Component註解，我將此過濾器定義為一個bean，使其能被APIGate伺服器識別並使用。
// 此過濾器負責在外部客戶端應用程式向APIGateway伺服器發送新請求時，產生追蹤ID或相關ID。
public class RequestTraceFilter implements GlobalFilter { // 當您想要讓APIGateway伺服器針對所有流量執行過濾器時，我們需要實現這個GlobalFilter介面
    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    // 當實作這個 GlobalFilter 介面時，需要覆寫一個 filter 方法並實作過濾器邏輯。
    // 請注意，Spring Cloud Gateway Server 的專案是建構在 Spring 反應式模組上 (如：ServerWebExchange、GatewayFilterChain、Mono)，而非傳統的 servlet 模組。
    // ServerWebExchange，我們可以直接存取請求和回應的相關資訊
    // GatewayFilterChain，可以配置任意數量的過濾器，我們將配置自訂過濾器，而APIGateway內部也已經有一些預定義的過濾器。這些過濾器會以鏈式方式串聯運作。
    // return chain.filter(exchange); 這就說明了為什麼在執行完自訂過濾器後，我們需要透過過濾器鏈來呼叫下一個過濾器。
    /*
     * 由於這個方法不需要返回具體內容，我們需要使用 Mono<Void> 類型。
     * void 表示方法不會返回任何內容，我們只是要呼叫下一個過濾器。
     * 在非同步程式中，我們應該使用 Mono 或 Flux 這兩種響應式類型。
     * Mono 用於表示單一物件，而 Flux 則用於表示物件集合。
     * 在這個情況下，因為我們既沒有返回物件也沒有集合，所以選擇使用 Mono。
    * */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders(); // 從ServerWebExchange中獲取請求，並從中擷取標頭資訊。
        if (isCorrelationIdPresent(requestHeaders)) { // 我們需要檢查所有Http請求標頭，確認是否已經設置了追蹤ID
            // 當我收到一個 true 時，這表示請求中已經存在相同名稱的標頭。
            // 在這種情況下，我們不需要再次生成關聯ID，這對於確認APIGateway 伺服器是否已經處理過該請求並添加了標頭非常有幫助
            // 有時因某些因素會發生重定向，導致APIGateway伺服器再次收到相同的請求。
            // 在這種情況下，如果APIGateway伺服器嘗試重新生成標頭值，可能會產生與之前相同的值，這是我們想要避免的情況。
            logger.debug("neuxBank-correlation-id found in RequestTraceFilter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            // 簡而言之，這個過濾器會產生一個新的關聯ID（或追蹤ID），並將其設定在請求標頭中。
            String correlationID = generateCorrelationId(); // 產生一個隨機的關聯ID
            exchange = filterUtility.setCorrelationId(exchange, correlationID); // 產生correlationId後，我們會使用FilterUtility中的setCorrelationId()方法，將此ID與ServerWebExchange一起設置。
            logger.debug("neuxBank-correlation-id generated in RequestTraceFilter : {}", correlationID);
        }
        return chain.filter(exchange); // 對於這個過濾器方法，我必須回傳與接收到相同的exchange物件。這就是為什麼我們要確保在最後加上return語句。
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        // 基於RequestTraceFilter中這個方法的回傳值，系統會據此判斷布林值為true或false。
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    private String generateCorrelationId() {
        // 透過Java內建的randomUUID()方法，我們可以產生一個隨機的關聯ID。
        return java.util.UUID.randomUUID().toString();
    }


}
