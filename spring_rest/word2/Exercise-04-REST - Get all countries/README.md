# Exercise 04 - REST Get all countries

Replaces `country-beans.xml` with `country.xml`, which defines a
`util:list` of `Country` beans. `CountryController` gains
`GET /countries`.

## Folder structure
```
Exercise-04-REST - Get all countries/
├── pom.xml
└── src/main/
    ├── java/com/cognizant/springlearn/
    │   ├── SpringLearnApplication.java   (@ImportResource loads country.xml)
    │   ├── controller/HelloController.java
    │   ├── controller/CountryController.java
    │   └── model/Country.java
    └── resources/
        ├── application.properties
        └── country.xml
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
curl http://localhost:8083/countries
```

## Expected Output
```
[{"code":"IN","name":"India"},{"code":"US","name":"United States"},{"code":"JP","name":"Japan"},{"code":"DE","name":"Germany"}]
```
