# Hands on 4 - Spring Core: Load Country from Spring Configuration XML

## Folder structure
```
Exercise-04-Spring Core - Load Country from Spring Configuration XML/
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
    │       └── country.xml
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

## Expected output (console, DEBUG lines abbreviated)
```
...
INFO  ... displayDate      | START
DEBUG ... displayDate      | Parsed Date : Mon Dec 31 00:00:00 IST 2018
INFO  ... displayDate      | END
INFO  ... displayCountry   | START
DEBUG ... Country          | Inside Country Constructor.
DEBUG ... Country          | Inside setCode() method.
DEBUG ... Country          | Inside setName() method.
DEBUG ... displayCountry   | Country : Country [code=IN, name=India]
INFO  ... displayCountry   | END
```

## Notes
- `Country.java` is a POJO with `code` and `name` fields, a no-arg
  constructor, getters/setters, and `toString()` - every one of them logs
  a `DEBUG` message when invoked.
- `country.xml` defines the `country` bean using `<property>` (setter)
  injection: `code=IN`, `name=India`.
- `displayCountry()` loads `country.xml`, calls
  `context.getBean("country", Country.class)`, and logs the result.
- Watching the log output shows exactly which Spring lifecycle
  methods (constructor, then each setter) are invoked, and in what order,
  when `context.getBean()` is called.
