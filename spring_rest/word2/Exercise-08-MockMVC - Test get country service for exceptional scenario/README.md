# Exercise 08 - MockMVC: Test get country service for exceptional scenario

Adds `testGetCountryException()` to `SpringLearnApplicationTests`, requesting an unknown country code and asserting the HTTP 404 response produced by `CountryNotFoundException` (Exercise 06).

## Folder structure
```
Exercise-08-MockMVC - Test get country service for exceptional scenario/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── SpringLearnApplication.java
    │   │   ├── controller/HelloController.java
    │   │   ├── controller/CountryController.java
    │   │   ├── service/CountryService.java
    │   │   ├── exception/CountryNotFoundException.java
    │   │   └── model/Country.java
    │   └── resources/
    │       ├── application.properties
    │       └── country.xml
    └── test/
        └── java/com/cognizant/springlearn/SpringLearnApplicationTests.java
```

## Build & run tests
```
mvn clean test
```

## Expected Output (console tail)
```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
All three tests (`contextLoads`, `testGetCountry`, `testGetCountryException`) pass. The exception test hits GET /countries/xx and asserts HTTP 404 with reason "Country not found" (see the note in the test class about the 400/404 mismatch in the original exercise document).
