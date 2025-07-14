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
