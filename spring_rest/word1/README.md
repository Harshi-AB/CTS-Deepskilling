# Cognizant Digital Nurture 5.0 - Deep Skilling - Spring REST Hands-on

This repository contains 6 independent, self-contained **Spring Boot + Maven**
projects, one per hands-on exercise from the "Spring REST" hand-out. Each
exercise builds on the previous one (they all extend the same
`SpringLearnApplication` class as described in the document), so later
exercises include the complete, cumulative code from earlier ones.

| # | Folder | Exercise |
|---|--------|----------|
| 1 | `Exercise-01-Create a Spring Web Project using Maven` | Create a Spring Web project via Spring Initializr / Maven |
| 2 | `Exercise-02-Spring Core - Load SimpleDateFormat from Spring Configuration XML` | Load a `SimpleDateFormat` bean from XML |
| 3 | `Exercise-03-Spring Core - Incorporate Logging` | SLF4J logging via `application.properties` |
| 4 | `Exercise-04-Spring Core - Load Country from Spring Configuration XML` | Load a custom `Country` bean via setter injection |
| 5 | `Exercise-05-Spring Core - Demonstration of Singleton Scope and Prototype Scope` | Singleton vs. prototype bean scope |
| 6 | `Exercise-06-Spring Core - Load list of countries from Spring Configuration XML` | Load a `List<Country>` built with `<list>`/`<ref>` |

## Prerequisites

- JDK 11 (or later)
- Apache Maven 3.6+ (each project uses its own `pom.xml`; no Maven wrapper is
  bundled - install Maven locally, or add `mvn -N io.takari:maven:wrapper`
  yourself if your environment requires a wrapper)
- Internet access (or an internal Cognizant Maven proxy/mirror) the first time
  you build, so Maven can download `spring-boot-starter-parent`,
  `spring-boot-starter-web`, `spring-boot-devtools`, and
  `spring-boot-starter-test` from Maven Central.

  If you are behind the Cognizant corporate proxy, build with:
  ```
  mvn clean package -Dhttp.proxyHost=proxy.cognizant.com -Dhttp.proxyPort=6050 -Dhttps.proxyHost=proxy.cognizant.com -Dhttps.proxyPort=6050
  ```

## How to build / run any exercise

```bash
cd "Exercise-0X-<exercise name>"
mvn clean package
mvn spring-boot:run
```

or, to run the packaged jar directly:

```bash
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

## Importing into Eclipse / IntelliJ

- **Eclipse**: `File > Import > Maven > Existing Maven Projects` > browse to
  the exercise folder > Finish.
- **IntelliJ IDEA**: `File > Open` > select the exercise folder's `pom.xml`
  > "Open as Project".

Each exercise's own `README.md` has its exact folder structure, build/run
commands, and expected console output.
