# Spring Data JPA Concepts — Cognizant Digital Nurture 5.0 Deep Skilling

Ten standalone, hand-rolled Java projects that recreate the core concepts of
**Spring Data JPA** (Entities, Repositories, CRUD, derived Query Methods,
Pagination & Sorting, Auditing, Projections, DataSource configuration, and
Hibernate-specific features) using **only core Java + core JDBC + MySQL** —
no Maven, no Gradle, no Spring framework jars, and no `package` declarations,
exactly as required for the Deep Skilling submission format.

Because the real `javax.persistence`/`jakarta.persistence` annotations and the
real Spring Data JPA interfaces live in jars that Maven/Gradle would normally
pull in, every exercise defines its **own** minimal, functionally equivalent
annotations (`@Entity`, `@Id`, `@Column`, ...) and its **own** repository
interfaces (`Repository`, `CrudRepository`, `JpaRepository`) — then wires them
up with a small reflection-based engine (`SimpleJpaRepository` +
`RepositoryFactory`, built on `java.lang.reflect.Proxy`) so that repository
interfaces get a working implementation automatically, just like real Spring
Data JPA does.

## One-time setup (needed for every exercise)

1. **Install MySQL** locally (or point at any reachable MySQL 8.x server).
2. **Download the MySQL JDBC driver** (`mysql-connector-j-8.x.x.jar`) from
   https://dev.mysql.com/downloads/connector/j/ — it cannot be fetched via
   Maven here since the project intentionally avoids Maven/Gradle.
3. Run the `schema.sql` inside the exercise folder you're working in:
   ```
   mysql -u root -p < schema.sql
   ```
4. Open `DBConfig.java` (Exercises 01–08) or `application.properties`
   (Exercises 09–10) and update the URL / username / password to match your
   MySQL instance.

## Compiling and running any exercise

Every exercise is self-contained — no shared classpath across folders.

```bash
cd "Exercise-XX-.../"
javac -cp .:mysql-connector-j-8.x.x.jar *.java
java  -cp .:mysql-connector-j-8.x.x.jar Main
```
(On Windows use `;` instead of `:` in `-cp`.)

> All ten projects have already been compiled with a plain `javac *.java`
> (no MySQL driver needed at *compile* time — only `java.sql.*` interfaces
> are used, the driver is loaded reflectively at *runtime*). Compilation was
> verified end-to-end for all 10 exercises using OpenJDK 21.

## Exercise index

| # | Folder | Concept | Key new files |
|---|--------|---------|----------------|
| 01 | Overview and Setup | Plain JDBC connectivity | `DBConfig`, `DBConnectionUtil` |
| 02 | Creating Entities | Custom `@Entity`/`@Column`/`@ManyToOne` annotations | `Employee`, `Department` |
| 03 | Creating Repositories | Repository interface hierarchy | `Repository`, `CrudRepository`, `JpaRepository` |
| 04 | Implementing CRUD Operations | Reflection-based generic CRUD + dynamic proxy | `SimpleJpaRepository`, `RepositoryFactory` |
| 05 | Defining Query Methods | Method-name-to-SQL parsing | `DerivedQueryExecutor` |
| 06 | Pagination and Sorting | `Sort` / `Pageable` / `Page<T>` | `Sort`, `PageRequest`, `Page` |
| 07 | Enabling Entity Auditing | `@CreatedDate` / `@LastModifiedDate` | `AuditingEntityListener` |
| 08 | Creating Projections | Interface-based dynamic projections | `ProjectionFactory` |
| 09 | Customizing Data Source Configuration | Externalized config + connection pool | `application.properties`, `SimpleConnectionPool` |
| 10 | Hibernate-Specific Features | L1/L2 cache, dirty-checking, batch inserts | `Session`, `SecondLevelCache`, `BatchInsertProcessor` |

Each exercise folder also has its own `README.md` with the exact folder
structure, compile/run commands, and expected console output.
