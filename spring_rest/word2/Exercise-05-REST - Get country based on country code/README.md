# Exercise 05 - REST Get country based on country code

Adds `CountryService`, which looks up a country by code
(case-insensitive, via Stream/lambda). `CountryController` gains
`GET /countries/{code}`.

## Folder structure
```
Exercise-05-REST - Get country based on country code/
├── pom.xml
└── src/main/
    ├── java/com/cognizant/springlearn/
    │   ├── SpringLearnApplication.java
    │   ├── controller/HelloController.java
    │   ├── controller/CountryController.java
    │   ├── service/CountryService.java
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
curl http://localhost:8083/countries/in
curl http://localhost:8083/countries/IN
```

## Expected Output (both requests)
```
{"code":"IN","name":"India"}
```
