# Exercise 04 - Many-to-One: Employee -> Department

## Folder structure
```
Exercise-04-Implement many to one relationship between Employee and Department/
├── README.md
├── sql/
│   └── payroll.sql   -- creates department, skill, employee, employee_skill + seed data
├── lib/              -- put mysql-connector-j-<version>.jar here
└── src/
    ├── Entity.java, Table.java, Id.java, Column.java, GeneratedValue.java,
    │   GenerationType.java, Transient.java, ManyToOne.java, OneToMany.java,
    │   ManyToMany.java, JoinColumn.java, JoinTable.java, FetchType.java,
    │   Service.java, Transactional.java     -- hand-written JPA/Spring-style annotations
    ├── Repository.java, RepositoryFactory.java, RepositoryInvocationHandler.java,
    │   QueryMethodParser.java, EntityUtils.java, DBConnection.java,
    │   LazyInitializationException.java, Logger.java   -- shared mini-ORM engine
    ├── Department.java            -- @Entity, the "one" side
    ├── Employee.java              -- @Entity, adds @ManyToOne Department + @JoinColumn
    ├── DepartmentRepository.java
    ├── EmployeeRepository.java
    ├── DepartmentService.java     -- @Service, get()/save() with @Transactional
    ├── EmployeeService.java       -- @Service, get()/save() with @Transactional
    └── OrmLearnApplication.java   -- testGetEmployee / testAddEmployee / testUpdateEmployee
```

`Employee.department` is annotated `@ManyToOne` with `@JoinColumn(name = "em_dp_id")`.
`@ManyToOne` defaults to `FetchType.EAGER` per the JPA spec, so every employee
loaded through `EmployeeService`/`EmployeeRepository` automatically has its
`Department` populated - no extra query written by hand.

`EmployeeService` and `DepartmentService` mirror the hands-on's exact
`@Service` / `@Transactional` get()/save() pattern. Since there is no Spring
container in a plain-Java project, each service builds its own repository
via `RepositoryFactory` instead of `@Autowired` - `OrmLearnApplication`
still creates the services with `new EmployeeService()` the same way the
hands-on assigns them "from the context".

## Setup
1. Install MySQL and start the server.
2. Download the MySQL Connector/J JAR (https://dev.mysql.com/downloads/connector/j/)
   and place it in `lib/`, e.g. `lib/mysql-connector-j-8.4.0.jar`.
3. Load schema + seed data:
   ```
   mysql -u root -p < sql/payroll.sql
   ```
4. Update credentials in `src/DBConnection.java` if needed (default root/root).

## Compilation command
```
javac -d bin src/*.java
```

## Run command
Linux/macOS:
```
java -cp "bin:lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```
Windows:
```
java -cp "bin;lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```

## Expected output
```
[INFO ] OrmLearnApplication - Start
[INFO ] EmployeeService - Start
[INFO ] EmployeeService - End
[DEBUG] OrmLearnApplication - Employee:Employee{id=1, name='Alice Johnson', salary=75000.0, permanent=true, dateOfBirth=1990-05-10}
[DEBUG] OrmLearnApplication - Department:Department{id=1, name='Engineering'}
[INFO ] OrmLearnApplication - End
[INFO ] OrmLearnApplication - Start
[INFO ] DepartmentService - Start
[INFO ] DepartmentService - End
[INFO ] EmployeeService - Start
[INFO ] EmployeeService - End
[DEBUG] OrmLearnApplication - Employee:Employee{id=5, name='Emma Wilson', salary=68000.0, permanent=true, dateOfBirth=<today's date>}
[INFO ] OrmLearnApplication - End
[INFO ] OrmLearnApplication - Start
[INFO ] EmployeeService - Start
[INFO ] EmployeeService - End
[INFO ] DepartmentService - Start
[INFO ] DepartmentService - End
[INFO ] EmployeeService - Start
[INFO ] EmployeeService - End
[DEBUG] OrmLearnApplication - Employee:Employee{id=2, name='Bob Smith', salary=65000.0, permanent=false, dateOfBirth=1992-08-21}
[INFO ] OrmLearnApplication - End
```
(The generated employee id in the middle block increments on every re-run
against the same database - that's expected AUTO_INCREMENT behaviour.)

Verified against a real local MySQL 8.0 instance while this project was generated.
