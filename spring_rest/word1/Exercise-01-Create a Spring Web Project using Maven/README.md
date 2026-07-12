# Hands on 1 - Create a Spring Web Project using Maven

## Folder structure
```
Exercise-01-Create a Spring Web Project using Maven/
├── pom.xml
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   └── SpringLearnApplication.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/cognizant/springlearn/
            └── SpringLearnApplicationTests.java
```

## Build command
```
mvn clean package
```

## Run command
```
mvn spring-boot:run
```
or, after packaging:
```
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

## Expected output (console)
```
main() method of SpringLearnApplication invoked - starting application...

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.18)

...
Tomcat started on port(s): 8080 (http)
Started SpringLearnApplication in X.XXX seconds
SpringLearnApplication started successfully.
```

## Notes / SME walkthrough points
- `src/main/java` - application source code.
- `src/main/resources` - `application.properties`, static assets, templates.
- `src/test/java` - test source code (`SpringLearnApplicationTests`).
- `SpringLearnApplication.java` - `main()` calls `SpringApplication.run()`, which
  bootstraps the Spring `ApplicationContext` and starts the embedded Tomcat server.
- `@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`.
- `pom.xml` - inherits from `spring-boot-starter-parent` (dependency/version
  management) and declares `spring-boot-starter-web` + `spring-boot-devtools`.
- Run `mvn dependency:tree` to view the dependency hierarchy.
