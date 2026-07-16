# Engineering Concepts — Spring REST Deep Skilling Exercises

Seven exercises simulating Spring-style RESTful web services in **Core Java only**
(no Spring, no Maven/Gradle, no third-party JARs). Each project uses the JDK's
built-in `com.sun.net.httpserver.HttpServer` to run a real embedded HTTP server,
so requests can be tested with `curl`, Postman, or a browser.

## Common rules across every exercise
- No `package` declarations.
- Compile with: `javac *.java`
- Run with: `java <MainClassName>`
- Server listens on `http://localhost:8080`
- Stop the server with `Ctrl+C`

## Exercises

| Folder | Main class | What it demonstrates |
|---|---|---|
| Exercise-01 | `ResourceNamingGuidelinesServer` | Proper REST URI naming: plural nouns, resource hierarchy, query-param filtering |
| Exercise-02 | `CountryPostService` | Handling a POST request to create a Country |
| Exercise-03 | `CountryBeanService` | Reading posted JSON data as a bound Java bean |
| Exercise-04 | `CountryCodeValidationService` | Validating country code format before persisting |
| Exercise-05 | `GlobalExceptionHandlerService` | Centralized/global exception handling for validation errors |
| Exercise-06 | `EmployeeUpdateService` | PUT service to update an existing Employee |
| Exercise-07 | `EmployeeDeleteService` | DELETE service to remove an Employee |

Each exercise folder is fully self-contained — copy any one folder on its own,
`cd` into it, `javac *.java`, `java <MainClassName>`, and it runs.
