# Exercise 02 - Hello World RESTful Web Service

Minimal Spring Boot REST project. `HelloController` exposes `GET /hello`.

## Folder structure
```
Exercise-02-Hello World RESTful Web Service/
├── pom.xml
└── src/
    └── main/
        ├── java/com/cognizant/springlearn/
        │   ├── SpringLearnApplication.java
        │   └── controller/HelloController.java
        └── resources/application.properties
```

## Build (requires Maven + internet access to Maven Central)
```
mvn clean install
```

## Run
```
mvn spring-boot:run
```
or
```
java -jar target/spring-learn.jar
```

## Test it
Open a browser or run:
```
curl http://localhost:8083/hello
```

## Expected Output
```
Hello World!!
```
