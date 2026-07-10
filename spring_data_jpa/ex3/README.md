# SpringData_JPA_Concepts

Complete, independent, ready-to-run Java projects for the 6 Cognizant
Digital Nurture 5.0 "Spring-Data-JPA" hands-on exercises.

Since Spring Data JPA's `@Query`/HQL/JPQL/native-query/Criteria-Query
features are normally backed by Hibernate acting as the JPA provider,
every project here uses **plain Hibernate ORM (as the JPA implementation)
+ MySQL Connector/J**, driven directly through `javax.persistence`
(`EntityManager`, `EntityManagerFactory`, `TypedQuery`, Criteria API)
instead of the Spring Framework - matching the "No Maven / No Gradle / no
package statements / Core MySQL only" constraints while still exercising
the exact concepts (HQL, JPQL, `@Query`-style repository methods, fetch
joins, native queries, Criteria Query) described in the document.

## Folder structure

```
SpringData_JPA_Concepts/
├── Exercise-01-Introduction to HQL and JPQL/
├── Exercise-02-Get all permanent employees using HQL/
├── Exercise-03-Fetch quiz attempt details using HQL/
├── Exercise-04-Get average salary using HQL/
├── Exercise-05-Get all employees using Native Query/
└── Exercise-06-Criteria Query and Skill/
```

Every project folder is fully self-contained:

```
Exercise-XX-.../
├── META-INF/
│   └── persistence.xml   <- JPA config (MySQL connection + entity list)
├── lib/
│   └── README.txt        <- list of jars to download (Hibernate, MySQL Connector/J, ...)
├── sql/
│   └── schema.sql         <- CREATE DATABASE / CREATE TABLE / sample INSERT statements
├── *.java                 <- entities, repositories, services, main class (NO package statement)
└── README.md               <- folder structure, code summary, compile/run commands, expected output
```

## How to run ANY exercise (same 4 steps every time)

1. `mysql -u root -p < sql/schema.sql` (run from inside that exercise's folder)
2. Download the jars listed in `lib/README.txt` into that exercise's `lib/` folder
   (the SAME jar set is reused by every exercise - download once, copy into each folder)
3. Edit `META-INF/persistence.xml` if your MySQL username/password differ from `root`/`root`
4. From inside the exercise folder:
   ```bash
   javac -cp "lib/*" *.java
   java  -cp ".:lib/*" OrmLearnApplication
   ```
   (Windows: use `;` instead of `:` in the run command's `-cp` value)

See each exercise's own `README.md` for the exact repository/service methods
implemented, the HQL/JPQL/native/Criteria query used, and the expected
console output for that exercise.

## Exercise summary

| # | Exercise | Core concept |
|---|----------|---------------|
| 1 | Introduction to HQL and JPQL | HQL vs JPQL syntax differences, HQL-only bulk INSERT |
| 2 | Get all permanent employees using HQL | `@Query`-style HQL + `left join fetch` to solve N+1 |
| 3 | Fetch quiz attempt details using HQL | Deep multi-table HQL join across 6 entities with `fetch` |
| 4 | Get average salary using HQL | HQL aggregate function `AVG()` + named parameter `:id` |
| 5 | Get all employees using Native Query | `nativeQuery = true` equivalent via `createNativeQuery` |
| 6 | Criteria Query and Skill | `CriteriaBuilder`/`CriteriaQuery`/`Root`/`Predicate` dynamic WHERE clause, incl. filtering by Skill |
