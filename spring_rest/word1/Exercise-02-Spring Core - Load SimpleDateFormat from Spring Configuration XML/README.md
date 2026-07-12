# Hands on 2 - Spring Core: Load SimpleDateFormat from Spring Configuration XML

## Folder structure
```
Exercise-02-Spring Core - Load SimpleDateFormat from Spring Configuration XML/
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
SpringLearnApplication started successfully.
Parsed Date: Mon Dec 31 00:00:00 IST 2018
```
(Exact day-of-week/timezone text depends on your machine's default locale/timezone -
the date value 31 Dec 2018 will always be correct.)

## Notes
- `date-format.xml` defines a `dateFormat` bean of type `java.text.SimpleDateFormat`
  configured via constructor-arg with pattern `dd/MM/yyyy`.
- `displayDate()` loads this bean with `ClassPathXmlApplicationContext` and calls
  `context.getBean("dateFormat", SimpleDateFormat.class)`.
