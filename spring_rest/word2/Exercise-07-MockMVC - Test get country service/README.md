# Exercise 07 - MockMVC: Test get country service

Adds `SpringLearnApplicationTests` under `src/test/java`, using
`@AutoConfigureMockMvc` to drive the controller through Spring MVC's
dispatcher without starting a real HTTP server.

## Folder structure
```
Exercise-07-MockMVC - Test get country service/
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
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
Both `contextLoads` and `testGetCountry` pass.
