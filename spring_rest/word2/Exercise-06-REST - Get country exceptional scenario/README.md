# Exercise 06 - REST Get country exceptional scenario

Adds `CountryNotFoundException` (`@ResponseStatus(NOT_FOUND, reason="Country not found")`).
`CountryService.getCountry()` now throws it instead of returning null.

## Folder structure
```
Exercise-06-REST - Get country exceptional scenario/
├── pom.xml
└── src/main/
    ├── java/com/cognizant/springlearn/
    │   ├── SpringLearnApplication.java
    │   ├── controller/HelloController.java
    │   ├── controller/CountryController.java
    │   ├── service/CountryService.java
    │   ├── exception/CountryNotFoundException.java
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
curl -i http://localhost:8083/countries/xx
```

## Expected Output
```
HTTP/1.1 404
...
{"timestamp":"...","status":404,"error":"Not Found","message":"Country not found","path":"/countries/xx"}
```
