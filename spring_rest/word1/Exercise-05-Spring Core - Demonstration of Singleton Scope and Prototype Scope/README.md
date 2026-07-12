# Hands on 5 - Spring Core: Demonstration of Singleton Scope and Prototype Scope

## Folder structure
```
Exercise-05-Spring Core - Demonstration of Singleton Scope and Prototype Scope/
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
    │       ├── country.xml              (default = singleton scope)
    │       └── country-prototype.xml    (scope="prototype")
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

## Expected output (console, abbreviated)
```
INFO  ... displayCountry               | START
DEBUG ... Country                      | Inside Country Constructor.   <-- called ONCE
DEBUG ... Country                      | Inside setCode() method.
DEBUG ... Country                      | Inside setName() method.
DEBUG ... displayCountry               | Country : Country [code=IN, name=India]
DEBUG ... displayCountry               | anotherCountry : Country [code=IN, name=India]
DEBUG ... displayCountry               | country == anotherCountry (singleton scope) : true
INFO  ... displayCountry               | END
INFO  ... displayCountryPrototypeScope | START
DEBUG ... Country                      | Inside Country Constructor.   <-- called TWICE
DEBUG ... Country                      | Inside setCode() method.
DEBUG ... Country                      | Inside setName() method.
DEBUG ... Country                      | Inside Country Constructor.
DEBUG ... Country                      | Inside setCode() method.
DEBUG ... Country                      | Inside setName() method.
DEBUG ... displayCountryPrototypeScope | country == anotherCountry (prototype scope) : false
INFO  ... displayCountryPrototypeScope | END
```

## Notes
- **Singleton scope** (`country.xml`, no `scope` attribute = default):
  Spring creates exactly ONE instance of the bean per `ApplicationContext`.
  Calling `getBean()` twice returns the SAME object reference
  (`country == anotherCountry` is `true`), and the constructor log line
  ("Inside Country Constructor.") appears only once.
- **Prototype scope** (`country-prototype.xml`, `scope="prototype"`):
  Spring creates a NEW instance every time `getBean()` is called.
  `country == anotherCountry` is `false`, and the constructor log line
  appears twice.
