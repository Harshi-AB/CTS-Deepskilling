# Spring Data JPA - Hands-on Exercises (Cognizant Digital Nurture 5.0)

Six independent, self-contained Java projects implementing every scenario in
the "Spring Data JPA - Hands-on" document, without Maven/Gradle, without
Spring or Hibernate JARs, and without package declarations.

```
SpringData_JPA_Concepts/
├── Exercise-01-Spring Data JPA - Write queries on country table using Query Methods
├── Exercise-02-Write queries on stock table using Query Methods
├── Exercise-03-Create payroll tables and bean mapping
├── Exercise-04-Implement many to one relationship between Employee and Department
├── Exercise-05-Implement one to many relationship between Employee and Department
└── Exercise-06-Implement many to many relationship between Employee and Skill
```

Every project follows the same layout:
```
Exercise-NN-.../
├── README.md    -- folder structure, exact compile/run commands, expected output
├── sql/         -- schema + seed data (plain SQL, no ORM tooling involved)
├── lib/         -- put the MySQL Connector/J JAR here (see below)
└── src/         -- all .java files, no package statement, one flat folder
```

## How this satisfies "no Spring/Hibernate, Core MySQL only"
Spring Data JPA's headline feature is **Query Methods** - deriving SQL purely
from a repository method's name (`findByCoNameContaining`, `findTop3ByOrderByStVolumeDesc`,
...) - plus **O/R mapping** via `@Entity`/`@ManyToOne`/`@OneToMany`/`@ManyToMany`.
Since the hands-on forbids the actual Spring/Hibernate JARs, every project
ships a small, from-scratch reimplementation of both mechanisms:

- Hand-written annotations (`Entity`, `Table`, `Id`, `Column`, `GeneratedValue`,
  `ManyToOne`, `OneToMany`, `ManyToMany`, `JoinColumn`, `JoinTable`, `Service`,
  `Transactional`, ...) - same names and semantics as `javax.persistence`/
  `org.springframework`, just declared locally.
- `RepositoryFactory` + `RepositoryInvocationHandler`: a `java.lang.reflect.Proxy`
  that implements every repository interface at runtime. `QueryMethodParser`
  parses method names such as `findByCoNameContainingOrderByCoNameAsc` into a
  `WHERE`/`ORDER BY`/`LIMIT` clause and runs it via plain JDBC
  (`java.sql.*`, bundled with the JDK) against MySQL.
- `LazyInitializationException` + fetch-type-aware relationship loading, so
  the `FetchType.LAZY` vs `FetchType.EAGER` exercises behave exactly like
  real Hibernate: an unloaded `@OneToMany`/`@ManyToMany` collection throws
  when accessed; an `EAGER` one is already populated.

The **only** external file used anywhere is the MySQL Connector/J JAR - the
JDBC driver required to talk to MySQL at all, since core Java ships no
database driver. It is needed at **runtime only** (`javac` never needs it,
since the code only imports `java.sql.*`).

## One-time setup (all exercises)
1. Install MySQL Server and make sure it's running.
2. Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
   (choose "Platform Independent", the `.jar` inside works everywhere).
   Copy it into each exercise's `lib/` folder you plan to run, e.g.
   `lib/mysql-connector-j-8.4.0.jar`.
3. Each exercise's `sql/` folder contains everything needed to (re)create its
   own tables - run the `.sql` file(s) listed in that exercise's own
   README.md before compiling/running.
4. If your MySQL username/password isn't `root`/`root`, edit `USER`/`PASSWORD`
   in `src/DBConnection.java` inside each exercise you run.

## Compiling and running any exercise
```
cd "Exercise-NN-..."
javac -d bin src/*.java
java -cp "bin:lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```
(On Windows, use `;` instead of `:` between classpath entries.)

Every project was actually compiled and executed against a real local
MySQL 8.0 instance while being generated - the "Expected output" section in
each exercise's README.md is the real captured console output, not a guess.

## Exercise index
| # | Folder | Concepts |
|---|---|---|
| 1 | Exercise-01 | Query Methods: `Containing`, `OrderBy...Asc`, `StartingWith` |
| 2 | Exercise-02 | Query Methods: `Between`, `GreaterThan`, `Top3...OrderBy...Desc` |
| 3 | Exercise-03 | `@Entity`/`@Table`/`@Id`/`@GeneratedValue`/`@Column` bean mapping, repositories |
| 4 | Exercise-04 | `@ManyToOne` + `@JoinColumn`, `@Service`/`@Transactional`, EAGER by default |
| 5 | Exercise-05 | `@OneToMany(mappedBy=...)`, LAZY -> `LazyInitializationException` -> EAGER fix |
| 6 | Exercise-06 | `@ManyToMany` + `@JoinTable`/`mappedBy`, LAZY -> EAGER, join-table inserts |
