# Exercise 03 - REST Country Web Service

Adds `Country` model, `country-beans.xml` (classic Spring XML bean
config), and `CountryController` exposing `GET /country`.

## Folder structure
```
Exercise-03-REST - Country Web Service/
├── pom.xml
└── src/main/
    ├── java/com/cognizant/springlearn/
    │   ├── SpringLearnApplication.java   (@ImportResource loads country-beans.xml)
    │   ├── controller/HelloController.java
    │   ├── controller/CountryController.java
    │   └── model/Country.java
    └── resources/
        ├── application.properties
        └── country-beans.xml
```

## Build
```
mvn clean install
```

## Run
```
mvn spring-boot:run
```

## Test it
```
curl http://localhost:8083/country
```

## Expected Output
```
{"code":"IN","name":"India"}
```
