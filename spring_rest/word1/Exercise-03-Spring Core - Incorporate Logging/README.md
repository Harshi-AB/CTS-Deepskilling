# Hands on 3 - Spring Core: Incorporate Logging

## Folder structure
```
Exercise-03-Spring Core - Incorporate Logging/
├── pom.xml
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   └── SpringLearnApplication.java
    │   └── resources/
    │       ├── application.properties
    │       └── date-format.xml
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
or:
```
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

## Expected output (console)
```
...
Started SpringLearnApplication in X.XXX seconds
250712|14:32:10.123|main                |INFO |c.c.springlearn.SpringLearnApplic|        displayDate|START
250712|14:32:10.140|main                |DEBUG|c.c.springlearn.SpringLearnApplic|        displayDate|Parsed Date : Mon Dec 31 00:00:00 IST 2018
250712|14:32:10.141|main                |INFO |c.c.springlearn.SpringLearnApplic|        displayDate|END
```
(Exact date/time/thread values will differ on your machine - the pattern and
log levels are what matter.)

## Notes
- `application.properties` sets:
  - `logging.level.org.springframework=info`
  - `logging.level.com.cognizant.springlearn=debug`
  - `logging.pattern.console=...` (custom console pattern)
- `SpringLearnApplication` now uses `org.slf4j.Logger` /
  `org.slf4j.LoggerFactory` instead of `System.out.println()`.
- `displayDate()` logs `INFO "START"` and `INFO "END"` around an internal
  `DEBUG` log of the parsed date.
- **From this exercise onward, `System.out.println()` is never used again -
  all output goes through the Logger.**
