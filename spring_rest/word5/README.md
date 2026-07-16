# Cognizant Digital Nurture 5.0 — Spring REST Deep Skilling Exercises

## Important note on build/run commands

The original spec requested `javac *.java` / `java MainClassName` with no
Maven. That is not possible here: these exercises are about **Spring
Security and JWT**, which are external libraries, not part of Core
Java. There is no way to get real Spring Boot, Spring Security or JWT
behaviour with `javac` alone.

So, per your choice, every exercise below is a **real, runnable Spring
Boot Maven project** using the actual `spring-security` and `jjwt`
libraries. Maven is what resolves those dependencies and compiles the
code — it plays the role `javac *.java` was meant to play, and
`mvn spring-boot:run` plays the role of `java MainClassName`.

Each project:
- Has **no package declarations** (all classes live in the default package), as requested.
- Is **fully self-contained** with its own `pom.xml` — no shared/multi-module setup.
- Compiles independently with `mvn clean package`.
- Runs independently with `mvn spring-boot:run` (or `java -jar target/exercise-XX.jar` after packaging).

## Prerequisites

- JDK 17+
- Maven 3.6+ (with internet access to Maven Central, to download Spring Boot/Spring Security/jjwt)

## Exercises

| # | Folder | Focus |
|---|--------|-------|
| 1 | Exercise-01-Securing RESTful Web Services with Spring Security | Basic HTTP Basic auth securing a REST endpoint |
| 2 | Exercise-02-Creating users and roles in Spring Security | Multiple in-memory users with USER/ADMIN roles |
| 3 | Exercise-03-Create authentication service that returns JWT | `JwtService` generates/validates signed JWTs |
| 4 | Exercise-04-Create authentication controller and configure it in SecurityConfig | `/authenticate` endpoint issuing JWTs |
| 5 | Exercise-05-Read Authorization header and decode the username and password | Manual Base64 decoding of a Basic auth header |
| 6 | Exercise-06-Authorize based on JWT | Full JWT filter authorizing requests by role |

Every project runs on its own port (8081–8086) so you can run several
of them side by side.

See the per-exercise instructions in the chat response for exact
`curl` commands and expected output for each project.
