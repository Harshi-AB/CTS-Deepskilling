# Engineering_Concepts

Cognizant Digital Nurture 5.0 – Deep Skilling – Microservices Concepts.

These three projects re-create the Spring Boot 3 / Spring Cloud "Edge
Services and API Gateway" exercises using **plain Core Java only**
(no Spring, no Maven, no Gradle), since Spring's runtime cannot be
demonstrated without its container. Each folder is a **fully
independent, no-package Java project** that compiles and runs on its
own.

| Folder | Concept demonstrated | Main class |
|---|---|---|
| `Exercise-01-Edge-Services-Routing-Filtering` | Request routing (Path predicates) + a global logging filter, modeled after Spring Cloud Gateway | `EdgeServiceGatewayDemo` |
| `Exercise-02-Load-Balancing-API-Gateway` | Client-side load balancing (`lb://` routes) with pluggable Random / Round-Robin strategies, modeled after Spring Cloud LoadBalancer | `LoadBalancerGatewayDemo` |
| `Exercise-03-Resilience-Patterns-API-Gateway` | Circuit Breaker (CLOSED → OPEN → HALF_OPEN → CLOSED) + a Time Limiter, modeled after Resilience4j | `ResilientGatewayDemo` |

## Build & run any exercise

```bash
cd Exercise-01-Edge-Services-Routing-Filtering
javac *.java
java EdgeServiceGatewayDemo
```

Repeat inside each of the other two folders (using their own main
class name). No external dependencies or internet access are required
— everything is Core Java (`java.util`, `java.util.concurrent`, etc.).
