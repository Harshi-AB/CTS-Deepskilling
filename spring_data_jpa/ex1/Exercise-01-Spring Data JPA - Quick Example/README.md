# Exercise 1 — Spring Data JPA - Quick Example

## What this demonstrates
The Spring Initializr "orm-learn" project from Hands-on 1, reproduced as plain,
package-less Core Java (no Maven, no external jars). Since we cannot open a
real JDBC connection to MySQL without the MySQL Connector/J jar, `MySQLDatabase`
simulates the database in memory and logs every operation in the exact
`Hibernate: <sql>` format that `logging.level.org.hibernate.SQL=trace` produces
against a real MySQL 8.0 instance.

- `Country.java` — persistence class with `@Entity` / `@Table` / `@Id` / `@Column`
- `CountryRepository.java` — `@Repository` interface extending `JpaRepository<Country, String>`
  (no impl class — see `JpaRepositoryProxyFactory`, which builds a JDK dynamic
  proxy for it at runtime, exactly like real Spring Data JPA)
- `CountryService.java` — `@Service` with `@Autowired` repository and
  `@Transactional getAllCountries()`
- `OrmLearnApplication.java` — `@SpringBootApplication` main class

## Folder structure
```
Exercise-01-Spring Data JPA - Quick Example/
├── src/
│   ├── ApplicationContext.java          (mini Spring IoC container)
│   ├── Autowired.java                   (custom annotation)
│   ├── Column.java                      (custom annotation)
│   ├── Country.java                     (persistence class)
│   ├── CountryRepository.java
│   ├── CountryService.java
│   ├── Entity.java                      (custom annotation)
│   ├── GeneratedValue.java              (custom annotation, unused here)
│   ├── Id.java                          (custom annotation)
│   ├── JpaRepository.java               (generic repository interface)
│   ├── JpaRepositoryProxyFactory.java    (dynamic proxy engine)
│   ├── Logger.java / LoggerFactory.java (SLF4J-style logging)
│   ├── MySQLDatabase.java               (in-memory MySQL simulator)
│   ├── OrmLearnApplication.java         (main class)
│   ├── Repository.java                  (custom annotation)
│   ├── Service.java                      (custom annotation)
│   ├── SpringApplication.java / SpringBootApplication.java
│   ├── Table.java                        (custom annotation)
│   └── Transactional.java                (custom annotation)
├── sql/
│   └── schema.sql                        (real MySQL DDL/DML reference)
└── expected_output.txt
```

## Compilation command
Run from inside `Exercise-01-Spring Data JPA - Quick Example/`:
```
javac -d out src/*.java
```

## Run command
```
java -cp out OrmLearnApplication
```

## Expected output
```
08-07-26 06:09:55.508 main                  INFO SpringApplication         Starting OrmLearnApplication using Java 21.0.11
08-07-26 06:09:55.514 main                  INFO SpringApplication         Started OrmLearnApplication - ApplicationContext initialised
08-07-26 06:09:55.516 main                  INFO OrmLearnApplication       Inside main
Hibernate: insert into country (co_code, co_name) values ('IN', 'India')
Hibernate: insert into country (co_code, co_name) values ('US', 'United States of America')
08-07-26 06:09:55.593 main                  INFO OrmLearnApplication       Start
Hibernate: select * from country
08-07-26 06:09:55.596 main                 DEBUG OrmLearnApplication       countries=[Country [code=IN, name=India], Country [code=US, name=United States of America]]
08-07-26 06:09:55.602 main                  INFO OrmLearnApplication       End
```
(Timestamps will differ on each run; see `expected_output.txt` for a captured run.)

## To run against a real MySQL database instead
Execute `sql/schema.sql` in MySQL Workbench / the `mysql` client, add the
`mysql-connector-j` jar to the classpath, and swap `MySQLDatabase`'s in-memory
maps for real `java.sql.Connection`/`PreparedStatement` calls using the
connection details from `sql/schema.sql`.
