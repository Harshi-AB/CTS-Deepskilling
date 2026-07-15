# Engineering_Concepts — Digital Nurture 5.0: Spring REST Concepts

Eight complete, standalone projects covering the Spring REST hands-on
exercises. Each exercise folder is self-contained and cumulative
(each one builds on the previous exercise's classes), so any single
folder can be opened, built, and run on its own.

| Exercise | Topic | Type |
|---|---|---|
| 01 | HTTP Request Response | Core Java (no framework) |
| 02 | Hello World RESTful Web Service | Spring Boot + Maven |
| 03 | REST - Country Web Service | Spring Boot + Maven |
| 04 | REST - Get all countries | Spring Boot + Maven |
| 05 | REST - Get country based on country code | Spring Boot + Maven |
| 06 | REST - Get country exceptional scenario | Spring Boot + Maven |
| 07 | MockMVC - Test get country service | Spring Boot + Maven |
| 08 | MockMVC - Test get country service for exceptional scenario | Spring Boot + Maven |

## Important note on tooling

Exercise 01 is genuinely framework-free, so it's plain Core Java —
compiles with `javac *.java`, runs with `java HttpRequestResponseDemo`.

Exercises 02-08 are Spring REST / MockMVC exercises. `@RestController`,
`@GetMapping`, Jackson JSON conversion, and MockMVC do not exist in
Core Java — they are provided by the Spring Framework, which is pulled
in as a Maven dependency. There's no way to give you these as
`javac`/`java`-only projects and have them actually be Spring
exercises, so each one is a normal Maven project instead:

```
mvn clean install        # compile + package
mvn spring-boot:run       # run the app on http://localhost:8083
mvn clean test             # run the MockMVC tests (Exercises 07-08)
```

You'll need Maven and internet access to Maven Central to build these
on your own machine — this sandbox couldn't reach Maven Central to
build/verify them, so double-check compilation locally (`mvn clean
install`) once you download them. The code was written and manually
reviewed for correctness, but wasn't machine-compiled here.

Each exercise folder also has its own `README.md` with the exact
folder structure, build/run commands, and expected output.
