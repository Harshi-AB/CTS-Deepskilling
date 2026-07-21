# Engineering_Concepts — Cognizant Digital Nurture 5.0 Microservices Exercises
### Plain Core-Java implementations (no Spring, no Maven/Gradle, no package statements)

Important note on environment: this sandbox only has a JRE, not a JDK
(`javac` and network access to install one are both unavailable here), so
the code below could not be compiler-verified inside this chat. Every file
was hand-reviewed (including an automated brace/parenthesis balance check
across all 30 files) for syntactic correctness. Please run `javac *.java`
in each folder on your own machine as the first step — that is standard
practice for any submission and will catch anything I may have missed.

Since "Core Java only" rules out Spring Boot, Eureka, Spring Cloud Gateway
and Resilience4j, each exercise re-creates the *behavior* those frameworks
provide using only the JDK:

| Framework concept | Plain-Java stand-in used here |
|---|---|
| REST controller (`@RestController`) | `com.sun.net.httpserver.HttpServer` (built into the JDK) |
| WebClient / OpenFeign | `HttpClientHelper` using `java.net.HttpURLConnection` |
| MySQL/PostgreSQL repository | `ConcurrentHashMap`-backed in-memory repository |
| Eureka Server | `ServiceRegistry` singleton (register/discover) |
| Spring Cloud Config Server | `ConfigServer` singleton (centralized key/value config) |
| Spring Cloud Gateway | `ApiGateway` (routing, path rewriting, filters) |
| Resilience4j Circuit Breaker | `CircuitBreaker` (CLOSED/OPEN/HALF_OPEN state machine) |

---

## Folder structure

```
Engineering_Concepts/
│
├── Exercise-01-1. Build a User and Order Management System/
│   ├── JsonUtil.java
│   ├── User.java
│   ├── Order.java
│   ├── HttpClientHelper.java
│   ├── UserService.java
│   ├── OrderService.java
│   └── UserOrderManagementSystem.java        <-- main class
│
├── Exercise-02-2. Inventory Management System with Service Discovery/
│   ├── JsonUtil.java
│   ├── HttpClientHelper.java
│   ├── Product.java
│   ├── ServiceRegistry.java
│   ├── ConfigServer.java
│   ├── ProductService.java
│   ├── InventoryService.java
│   └── InventoryManagementSystem.java        <-- main class
│
├── Exercise-03-3. Implement an API Gateway/
│   ├── JsonUtil.java
│   ├── HttpClientHelper.java
│   ├── Customer.java
│   ├── Bill.java
│   ├── CustomerService.java
│   ├── BillingService.java
│   ├── RateLimiter.java
│   ├── ResponseCache.java
│   ├── ApiGateway.java
│   └── ApiGatewaySystem.java                 <-- main class
│
└── Exercise-04-4. Resilient Microservices with Circuit Breaker/
    ├── PaymentGatewayException.java
    ├── ThirdPartyPaymentGateway.java
    ├── CircuitBreaker.java
    ├── FallbackMonitor.java
    ├── PaymentService.java
    └── PaymentResilienceSystem.java          <-- main class
```

Every folder is fully self-contained (its own copy of `JsonUtil.java` /
`HttpClientHelper.java` where needed) so each one compiles independently
with a plain `javac *.java` — no shared classpath required.

---

## Exercise 1 — User and Order Management System

**Design:** Two independent REST microservices running on different ports
in the same JVM. `OrderService` calls `UserService` over real HTTP
(`HttpClientHelper`, the WebClient/OpenFeign stand-in) to enrich an order
with its user's details — exactly the pattern WebClient/OpenFeign is used
for in the original exercise.

- `UserService` → `GET/POST /users`, `GET /users/{id}` on port **8081**
- `OrderService` → `GET/POST /orders`, `GET /orders/{id}` on port **8082**
  (internally calls User Service for `GET /orders/{id}`)

**Compile:**
```
cd "Exercise-01-1. Build a User and Order Management System"
javac *.java
```

**Run:**
```
java UserOrderManagementSystem
```

**Expected output (abridged):**
```
[UserService] Started on http://localhost:8081/users
[OrderService] Started on http://localhost:8082/orders

================ DEMO: REST client calls ================

>> GET /users
[{"id":1,"name":"Alice Johnson","email":"alice@example.com"},{"id":2,"name":"Bob Smith","email":"bob@example.com"}]

>> GET /users/1
{"id":1,"name":"Alice Johnson","email":"alice@example.com"}

>> POST /users  (create Charlie Davis)
{"id":3,"name":"Charlie Davis","email":"charlie@example.com"}

>> GET /orders
[{"id":1,"userId":1,"product":"Wireless Mouse","quantity":2,"status":"PLACED"},{"id":2,"userId":2,"product":"Mechanical Keyboard","quantity":1,"status":"PLACED"}]

>> GET /orders/1 (Order Service internally calls User Service)
{"id":1,"product":"Wireless Mouse","quantity":2,"status":"PLACED","user":{"id":1,"name":"Alice Johnson","email":"alice@example.com"}}

>> POST /orders (Charlie orders a Monitor)
{"id":3,"userId":3,"product":"4K Monitor","quantity":1,"status":"PLACED"}

>> GET /orders/3 (should show Charlie's user details embedded)
{"id":3,"product":"4K Monitor","quantity":1,"status":"PLACED","user":{"id":3,"name":"Charlie Davis","email":"charlie@example.com"}}

================ DEMO COMPLETE ================
Both microservices have been stopped.
```

---

## Exercise 2 — Inventory Management System with Service Discovery

**Design:** `ServiceRegistry` (Singleton) is the Eureka stand-in — both
services register themselves under a logical name at startup, and
`InventoryService` looks up `PRODUCT-SERVICE`'s current address at
request time rather than hardcoding it. `ConfigServer` (Singleton) is the
Config-Server stand-in, centrally holding ports and the
low-stock-threshold / restock-quantity business values used by both
services.

- `ProductService` → registers as `PRODUCT-SERVICE`, port **9081**
- `InventoryService` → registers as `INVENTORY-SERVICE`, port **9082**,
  discovers Product Service through `ServiceRegistry`

**Compile:**
```
cd "Exercise-02-2. Inventory Management System with Service Discovery"
javac *.java
```

**Run:**
```
java InventoryManagementSystem
```

**Expected output (abridged):**
```
================ Centralized Configuration ================
[ConfigServer] Centralized configuration:
   product-service.port = 9081
   inventory-service.port = 9082
   inventory.low-stock-threshold = 5
   inventory.default-restock-quantity = 20

================ Starting Microservices ================
[ServiceRegistry] Registered PRODUCT-SERVICE -> http://localhost:9081
[ProductService] Started on http://localhost:9081/products
[ServiceRegistry] Registered INVENTORY-SERVICE -> http://localhost:9082
[InventoryService] Started on http://localhost:9082/inventory

[ServiceRegistry] Current registrations:
   PRODUCT-SERVICE -> http://localhost:9081
   INVENTORY-SERVICE -> http://localhost:9082

================ DEMO: REST client calls ================

>> GET /inventory  (Inventory Service discovers Product Service)
{"lowStockThreshold":5,"products":[{"id":1,"name":"Wireless Mouse","price":19.99,"stock":50},{"id":2,"name":"Mechanical Keyboard","price":59.99,"stock":30},{"id":3,"name":"4K Monitor","price":249.99,"stock":3}]}

>> GET /inventory/3/check  (checking low-stock threshold)
{"productId":3,"stock":3,"threshold":5,"lowStock":true}

>> POST /inventory/3/restock  (restocks using ConfigServer value)
{"id":3,"name":"4K Monitor","price":249.99,"stock":23}

>> GET /inventory/3/check  (should no longer be low stock)
{"productId":3,"stock":23,"threshold":5,"lowStock":false}

================ DEMO COMPLETE ================
[ServiceRegistry] Deregistered PRODUCT-SERVICE
[ServiceRegistry] Deregistered INVENTORY-SERVICE
Both microservices have been stopped and deregistered.
```

---

## Exercise 3 — Implement an API Gateway

**Design:** `ApiGateway` is the single entry point (Spring Cloud Gateway
stand-in) in front of `CustomerService` and `BillingService`. Every
request goes through three filters in order: **rate limiting**
(`RateLimiter`, a token-bucket per client IP), **caching**
(`ResponseCache`, a 5-second TTL cache for GET responses), then **path
rewriting + routing** (`/gateway/customers/**` → `/api/customers/**` on
Customer Service, `/gateway/billing/**` → `/api/billing/**` on Billing
Service).

- `CustomerService` → port **7001**, `BillingService` → port **7002**
- `ApiGateway` → port **8080** (the only port a client should call)

**Compile:**
```
cd "Exercise-03-3. Implement an API Gateway"
javac *.java
```

**Run:**
```
java ApiGatewaySystem
```

**Expected output (abridged):**
```
[CustomerService] Started on http://localhost:7001/api/customers
[BillingService] Started on http://localhost:7002/api/billing
[ApiGateway] Started on http://localhost:8080/gateway

================ DEMO 1: Path Rewriting & Routing ================
>> GET /gateway/customers  (rewritten internally to /api/customers)
[ApiGateway] Routing /gateway/customers -> http://localhost:7001/api/customers (path rewritten)
[{"id":1,"name":"Alice Johnson","city":"Chennai"}, ... ]

>> GET /gateway/billing/2  (rewritten internally to /api/billing/2)
{"id":2,"customerId":2,"amount":89.0,"status":"PENDING"}

================ DEMO 2: Response Caching ================
>> GET /gateway/customers/1  (first call -> cache MISS, hits CustomerService)
{"id":1,"name":"Alice Johnson","city":"Chennai"}

>> GET /gateway/customers/1  (second call, within TTL -> cache HIT)
[ApiGateway] Cache HIT for /gateway/customers/1
{"id":1,"name":"Alice Johnson","city":"Chennai"}

================ DEMO 3: Rate Limiting ================
Bucket capacity = 3 tokens, refill = 1 token/sec.
Firing 5 rapid requests to /gateway/billing/1 ...

Request #1 -> {"id":1,"customerId":1,"amount":149.5,"status":"PAID"}
Request #2 -> {"id":1,"customerId":1,"amount":149.5,"status":"PAID"}
Request #3 -> {"id":1,"customerId":1,"amount":149.5,"status":"PAID"}
Request #4 -> {"error":"Too Many Requests - rate limit exceeded"}
Request #5 -> {"error":"Too Many Requests - rate limit exceeded"}

Waiting 2 seconds for the token bucket to partially refill...
>> GET /gateway/billing/1 (after waiting, should be allowed again)
{"id":1,"customerId":1,"amount":149.5,"status":"PAID"}

================ DEMO COMPLETE ================
All services and the gateway have been stopped.
```
*(Note: Request #2/#3 in the rate-limit demo are cache hits since caching runs before rate limiting on the response, but the token bucket still charges each request — exact numbers depend on system timing.)*

---

## Exercise 4 — Resilient Microservices with Circuit Breaker

**Design:** `CircuitBreaker` implements the real CLOSED → OPEN → HALF_OPEN
state machine (the Resilience4j stand-in). `ThirdPartyPaymentGateway`
simulates a slow API that deterministically fails its first 3 calls then
recovers, so the demo reliably shows the breaker tripping and recovering.
`FallbackMonitor` logs and stores every fallback event, then prints a
summary report — satisfying "log and monitor fallback events".

**Compile:**
```
cd "Exercise-04-4. Resilient Microservices with Circuit Breaker"
javac *.java
```

**Run:**
```
java PaymentResilienceSystem
```

**Expected output (abridged):**
```
================ Resilient Payment Processing Demo ================
Failure threshold: 3 consecutive failures trips the breaker
Reset timeout: 2000 ms before a HALF_OPEN trial is attempted
Simulated gateway will fail its first 3 calls, then recover.

>> Processing payment for order #1 (breaker state before call: CLOSED)
[FallbackMonitor] [...] Order #1 -> FALLBACK triggered (Third-party payment gateway timed out (simulated outage, call #1))
Order #1 -> FALLBACK (amount 100.0 queued for retry; customer notified payment is processing)

>> Processing payment for order #2 (breaker state before call: CLOSED)
... (similar fallback for call #2)

>> Processing payment for order #3 (breaker state before call: CLOSED)
[CircuitBreaker:PaymentGatewayBreaker] Failure threshold (3) reached -> CLOSED -> OPEN
... (fallback for call #3, breaker now OPEN)

>> Processing payment for order #4 (breaker state before call: OPEN)
[FallbackMonitor] [...] Order #4 -> FALLBACK triggered (circuit OPEN - third-party gateway skipped entirely)
Order #4 -> FALLBACK (amount 400.0 queued for retry; customer notified payment is processing)

Waiting for the reset timeout (2000 ms) to elapse...

>> Processing payment for order #5 (breaker state before call: OPEN)
[CircuitBreaker:PaymentGatewayBreaker] Timeout elapsed -> transitioning OPEN -> HALF_OPEN
[CircuitBreaker:PaymentGatewayBreaker] Trial call succeeded -> HALF_OPEN -> CLOSED
Order #5 -> SUCCESS (transactionId=TXN-...)

>> Processing payment for order #6 (breaker state before call: CLOSED)
Order #6 -> SUCCESS (transactionId=TXN-...)

[FallbackMonitor] ===== Fallback Event Summary =====
   [...] Order #1 -> FALLBACK triggered (...)
   [...] Order #2 -> FALLBACK triggered (...)
   [...] Order #3 -> FALLBACK triggered (...)
   [...] Order #4 -> FALLBACK triggered (circuit OPEN - third-party gateway skipped entirely)
Total fallback events: 4

Final circuit breaker state: CLOSED
================ DEMO COMPLETE ================
```

---

## Quick-start: compile & run everything

```bash
cd Engineering_Concepts
for dir in Exercise-*/ ; do
  echo "=== $dir ==="
  (cd "$dir" && javac *.java)
done
```
Then run each exercise's main class individually as shown above (each
starts its own HTTP servers on distinct ports, so they won't conflict if
run one after another; avoid running two exercises' servers at the exact
same time only if you reuse the same ports).
