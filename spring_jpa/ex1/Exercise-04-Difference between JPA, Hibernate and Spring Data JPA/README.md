# Exercise 4 — Difference between JPA, Hibernate and Spring Data JPA

## What this demonstrates
- **JPA** (`javax.persistence`, JSR 338) is a *specification* with no
  implementation of its own.
- **Hibernate** is an ORM tool that *implements* JPA (`Session`,
  `SessionFactory`, `Transaction` — see Exercises 2/3).
- **Spring Data JPA** is a further abstraction *over* an implementation like
  Hibernate that removes boilerplate (`JpaRepository`, `@Transactional`,
  `@Autowired` — see Exercises 1, 5-9).

This project runs **both** the plain-Hibernate `addEmployee()` and the
Spring-Data-JPA `addEmployee()` side by side against the same simulated
`EMPLOYEE` table, using the exact code snippets from the exercise document, so
you can see the boilerplate difference directly rather than just read about it.

- `HibernateEmployeeDao.java` — the Hibernate column from the doc: manual
  `session.openSession()` / `beginTransaction()` / `commit()` /
  `catch(HibernateException)` / `finally session.close()`.
- `EmployeeRepository.java` + `EmployeeService.java` — the Spring Data JPA
  column: a one-line `@Transactional addEmployee()` method with no manual
  session handling at all.

## Folder structure
```
Exercise-04-Difference between JPA, Hibernate and Spring Data JPA/
├── config/
│   └── hibernate.cfg.xml
├── src/
│   ├── AnnotationConfiguration.java / AnnotationEntityScanner.java  (Hibernate side)
│   ├── ApplicationContext.java / SpringApplication.java             (Spring side)
│   ├── Autowired.java / Service.java / Repository.java / Transactional.java / SpringBootApplication.java
│   ├── Column.java / Entity.java / GeneratedValue.java / Id.java / Table.java
│   ├── ComparisonDemo.java           (main class - runs both approaches)
│   ├── ConnectionConfig.java
│   ├── Employee.java                 (shared annotated entity)
│   ├── EmployeeRepository.java       (Spring Data JPA repository)
│   ├── EmployeeService.java          (Spring Data JPA service)
│   ├── EntityMapping.java
│   ├── HibernateEmployeeDao.java     (plain Hibernate DAO)
│   ├── HibernateException.java
│   ├── HibernateXmlConfigReader.java
│   ├── JpaRepository.java / JpaRepositoryProxyFactory.java
│   ├── Logger.java / LoggerFactory.java
│   ├── MySQLDatabase.java
│   ├── Query.java / Session.java / SessionEntityMapper.java / SessionFactory.java / Transaction.java
└── expected_output.txt
```

## Compilation command
```
javac -d out src/*.java
```

## Run command
Must be run from this exercise's root folder:
```
java -cp out ComparisonDemo
```

## Expected output (abridged — see expected_output.txt for the full run)
```
... INFO ComparisonDemo  ===== JPA is a specification; Hibernate is an implementation of it =====
... INFO ComparisonDemo  --- Hibernate way: HibernateEmployeeDao.addEmployee() ---
Hibernate: insert into EMPLOYEE (id, first_name, last_name, salary) values ('1001', 'Zara', 'Ali', '1000')
... INFO ComparisonDemo  Employee persisted via plain Hibernate, id=1001
... INFO ComparisonDemo  --- Spring Data JPA way: EmployeeService.addEmployee() ---
Hibernate: insert into EMPLOYEE (id, first_name, last_name, salary) values ('5001', 'Daisy', 'Das', '5000')
... INFO ComparisonDemo  Employee persisted via Spring Data JPA, id=5001
Hibernate: select * from EMPLOYEE
... DEBUG ComparisonDemo All employees in table: [Employee [id=1001,...], Employee [id=5001,...]]
... INFO SessionFactory  SessionFactory closed
... INFO ComparisonDemo  ===== Summary =====
... INFO ComparisonDemo  Hibernate way   : 9 lines of session/transaction bookkeeping per method
... INFO ComparisonDemo  Spring Data JPA : 1 line - employeeRepository.save(employee)
```

## Note on the two id sequences
The Hibernate path and the Spring Data JPA path in this demo use two
independent id counters (1000s and 5000s) purely because they are two
separate simulated persistence contexts running in the same JVM process. In a
real MySQL database, both would share the same `AUTO_INCREMENT` counter on
the one physical `EMPLOYEE` table.
