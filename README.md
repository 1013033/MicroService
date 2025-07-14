<pre> ```mermaid
sequenceDiagram
    autonumber
    participant Client
    participant Gateway
    participant AccountsAPI
    participant KafkaTopic
    participant MessageService
    participant EmailFunction
    participant SmsFunction
    participant Keycloak

    %% === 用戶呼叫 Gateway ===
    Client->>Gateway: POST /neux/accounts/create (JWT)
    Gateway->>Keycloak: 驗證 JWT (realm_access)
    Keycloak-->>Gateway: 驗證通過

    Gateway->>AccountsAPI: 轉發 /api/create
    AccountsAPI->>AccountsAPI: 建立 Customer & Account
    AccountsAPI->>KafkaTopic: StreamBridge.send(send-communication)
    KafkaTopic->>MessageService: Topic send-communication

    MessageService->>EmailFunction: Function<AccountsMsgDto, AccountsMsgDto> email()
    EmailFunction-->>KafkaTopic: 發送到 send-email (若有)

    MessageService->>SmsFunction: Function<AccountsMsgDto, Long> sms()
    SmsFunction-->>KafkaTopic: 發送到 send-sms (若有)

    %% === 回應 ===
    AccountsAPI-->>Gateway: Response 201 Created
    Gateway-->>Client: Response 201 Created + neuxbank-correlation-id

    %% === 後續 Flow ===
    Note over EmailFunction, SmsFunction: 可改送實際 Email/SMS 或<br>後續服務消費
``` </pre>
## 系統上下游關係圖

```mermaid
flowchart TD
  subgraph Client[Client]
    A1[呼叫 API<br>JWT Token]
  end

  subgraph Gateway[API Gateway]
    B1[驗證 JWT<br>Keycloak]
    B2[產生<br>Correlation ID]
    B3[轉發到<br>下游]
  end

  subgraph Accounts[Accounts Service]
    C1[REST API<br>/api/create]
    C2[建立 Customer<br>& Account]
    C3[StreamBridge<br>send-communication]
  end

  subgraph Kafka[Kafka / RabbitMQ]
    D1[Topic: send-communication]
    D2[Topic: send-email]
    D3[Topic: send-sms]
  end

  subgraph Message[Message Service]
    E1[email() Function<br>AccountsMsgDto -> AccountsMsgDto]
    E2[sms() Function<br>AccountsMsgDto -> Long]
  end

  subgraph Keycloak[Keycloak Server]
    F1[驗證 JWT<br>回傳角色<br>realm_access]
  end

  %% ==== 邊 ====
  A1 --> B1 --> F1 --> B2 --> B3 --> C1 --> C2 --> C3 --> D1
  D1 --> E1 --> D2
  D1 --> E2 --> D3

  %% 回應線
  C1 -->|201 Created| B3 -->|201 Created + CorrelationID| A1

```
