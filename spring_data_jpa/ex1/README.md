# SpringData_JPA_Concepts

Complete, working, GitHub-ready projects for all 9 Cognizant Digital Nurture
5.0 Deep Skilling "Spring Data JPA" hands-on exercises.

## Important note on the "Core MySQL only" / "no external jars" constraints

These two constraints are in tension: Core Java has no built-in MySQL driver,
so real JDBC connectivity to MySQL requires the `mysql-connector-j` jar. Per
explicit instruction, **no jars** are used anywhere in this project. Instead:

- **`MySQLDatabase.java`** (duplicated into each exercise) is an in-memory
  stand-in for a real MySQL 8.0 database. Every operation prints the exact
  SQL statement it represents to the console, in the same format Hibernate's
  `logging.level.org.hibernate.SQL=trace` produces against a live database
  (e.g. `Hibernate: insert into country (co_code, co_name) values (...)`).
- Every exercise also ships a real **`sql/*.sql`** (or `config/*.xml`) file
  containing the actual MySQL DDL/DML or Hibernate configuration you would
  use against a real MySQL instance. Each README explains exactly what to
  swap in (`mysql-connector-j` on the classpath + real `java.sql.Connection`
  calls) to go from "simulated" to "real database."

This means every project **compiles and runs standalone with nothing but the
JDK** (`javac` / `java`, no Maven, no Gradle, no jars, no package
declarations), while still faithfully reproducing MySQL/Hibernate/Spring Data
JPA behavior — including realistic SQL logging, transactions, and dynamic
proxy-based repositories.

## How Spring/Hibernate/JPA are simulated

| Real technology | This project's stand-in |
|---|---|
| `@Entity` / `@Table` / `@Id` / `@Column` / `@GeneratedValue` | Custom `@interface` annotations, read via reflection |
| Spring's `@Repository`, `@Service`, `@Autowired`, `@Transactional` | Custom annotations + `ApplicationContext` (mini IoC container) |
| Spring Data JPA repository proxies | `JpaRepositoryProxyFactory` — a real `java.lang.reflect.Proxy` (GoF Proxy pattern), exactly how Spring Data JPA itself works |
| Hibernate `SessionFactory` / `Session` / `Transaction` | Hand-written classes of the same name and API, backed by `MySQLDatabase` |
| `hibernate.cfg.xml` / `*.hbm.xml` | Real Hibernate-format XML files, parsed with plain DOM (`javax.xml.parsers`) |
| MySQL database | `MySQLDatabase.java` (in-memory tables + SQL-style trace logging) |

## Exercises

| # | Folder | Covers |
|---|--------|--------|
| 1 | `Exercise-01-Spring Data JPA - Quick Example` | End-to-end Spring Data JPA quick start (`Country`/`CountryRepository`/`CountryService`) |
| 2 | `Exercise-02-Hibernate XML Config implementation walk through` | Classic Hibernate XML mapping (`hibernate.cfg.xml` + `Employee.hbm.xml`) |
| 3 | `Exercise-03-Hibernate Annotation Config implementation walk through` | Same CRUD walkthrough, annotation-driven mapping instead of XML |
| 4 | `Exercise-04-Difference between JPA, Hibernate and Spring Data JPA` | Side-by-side plain-Hibernate DAO vs. Spring Data JPA repository/service |
| 5 | `Exercise-05-Implement services for managing Country` | Loads the full 249-country dataset; `getAllCountries()` |
| 6 | `Exercise-06-Find a country based on country code` | Adds `findCountryByCode()` + `CountryNotFoundException` |
| 7 | `Exercise-07-Add a new country` | Adds `addCountry()` |
| 8 | `Exercise-08-Update a country based on code` | Adds `updateCountry()` |
| 9 | `Exercise-09-Delete a country based on code` | Adds `deleteCountry()` |

Exercises 5-9 are cumulative and each is fully self-contained (all files
needed are duplicated into every folder) so every project compiles and runs
independently, per the exercise instructions.

## Quick start (any exercise)
```bash
cd "Exercise-0N-<name>"
javac -d out -encoding UTF-8 src/*.java
java -Dstdout.encoding=UTF-8 -cp out <MainClassName>
```
See each exercise's own `README.md` for its exact main class name, folder
structure, and expected console output.
