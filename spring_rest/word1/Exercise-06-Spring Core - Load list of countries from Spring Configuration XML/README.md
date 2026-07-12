# Hands on 6 - Spring Core: Load list of countries from Spring Configuration XML

## Folder structure
```
Exercise-06-Spring Core - Load list of countries from Spring Configuration XML/
├── pom.xml
├── .gitignore
└── src/
    ├── main/
    │   ├── java/com/cognizant/springlearn/
    │   │   ├── Country.java
    │   │   └── SpringLearnApplication.java
    │   └── resources/
    │       ├── application.properties
    │       ├── date-format.xml
    │       ├── country.xml
    │       ├── country-prototype.xml
    │       └── country-list.xml
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

## Expected output (console, displayCountries() section, abbreviated)
```
INFO  ... displayCountries | START
DEBUG ... Country          | Inside Country Constructor.   (x4, one per country)
...
DEBUG ... displayCountries | Country : Country [code=IN, name=India]
DEBUG ... displayCountries | Country : Country [code=US, name=United States]
DEBUG ... displayCountries | Country : Country [code=DE, name=Germany]
DEBUG ... displayCountries | Country : Country [code=JP, name=Japan]
INFO  ... displayCountries | END
```

## Notes
- `country-list.xml` defines four separate `Country` beans (`in`, `us`,
  `de`, `jp`) plus a `countryList` bean of type `java.util.ArrayList`,
  built via `<constructor-arg><list><ref bean="..."/>...</list></constructor-arg>`.
- Each `<ref bean="xyz"/>` tells Spring to insert the already-defined
  bean with id `xyz` into the list (no new instance is created for the
  reference itself).
- `displayCountries()` retrieves the `countryList` bean and logs a
  `DEBUG` line for every country in it, satisfying the original goal of
  the airlines website drop-down needing all four countries.
