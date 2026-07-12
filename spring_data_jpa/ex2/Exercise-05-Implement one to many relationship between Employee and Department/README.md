# Exercise 05 - One-to-Many: Department -> Employees (LAZY vs EAGER)

## Folder structure
```
Exercise-05-Implement one to many relationship between Employee and Department/
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
    ├── Employee.java              -- @Entity, @ManyToOne back to Department
    ├── Department.java            -- @Entity, @OneToMany(mappedBy="department") - default LAZY
    ├── DepartmentEager.java       -- same table, @OneToMany(..., fetch = EAGER)
    ├── EmployeeRepository.java, DepartmentRepository.java, DepartmentEagerRepository.java
    ├── EmployeeService.java, DepartmentService.java, DepartmentEagerService.java
    └── OrmLearnApplication.java   -- testGetDepartment() / testGetDepartmentEager()
```

## The LAZY -> EAGER exercise, exactly as documented
1. `Department.employeeList` is `@OneToMany(mappedBy = "department")` with no
   explicit `fetch` - which defaults to `FetchType.LAZY` per the JPA spec.
   `testGetDepartment()` loads department id 1 and then calls
   `getEmployeeList()`, which throws `LazyInitializationException` because
   the collection was never populated - mirroring real Hibernate's
   behaviour when a lazy collection is touched with no open session.
2. `DepartmentEager` maps the *same* table but declares
   `@OneToMany(mappedBy = "department", fetch = FetchType.EAGER)`.
   `testGetDepartmentEager()` loads the same department through it and the
   employee list is already populated - no exception.

Both classes exist side by side purely so this generated project can show
the "before" and "after" in a single run. In your own copy, you can delete
`DepartmentEager.java`/`DepartmentEagerService.java`/`DepartmentEagerRepository.java`
and simply add `fetch = FetchType.EAGER` to `Department.employeeList` to
reproduce the exact one-file, two-step exercise from the document.

## Setup
1. Install MySQL and start the server.
2. Download the MySQL Connector/J JAR and place it in `lib/`.
3. Load schema + seed data:
   ```
   mysql -u root -p < sql/payroll.sql
   ```
4. Update credentials in `src/DBConnection.java` if needed.

## Compilation command
```
javac -d bin src/*.java
```

## Run command
```
java -cp "bin:lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```

## Expected output
```
[INFO ] OrmLearnApplication - Start
[INFO ] DepartmentService - Start
[INFO ] DepartmentService - End
[DEBUG] OrmLearnApplication - Department:Department{id=1, name='Engineering'}
Caught expected LazyInitializationException: failed to lazily initialize a collection: department.employeeList, could not initialize proxy - no session
[INFO ] OrmLearnApplication - End
[INFO ] OrmLearnApplication - Start
[INFO ] DepartmentEagerService - Start
[INFO ] DepartmentEagerService - End
[DEBUG] OrmLearnApplication - Department:Department{id=1, name='Engineering'}
[DEBUG] OrmLearnApplication - Employees:[Employee{id=1, name='Alice Johnson', salary=75000.0, permanent=true, dateOfBirth=1990-05-10}, Employee{id=2, name='Bob Smith', salary=65000.0, permanent=false, dateOfBirth=1992-08-21}]
[INFO ] OrmLearnApplication - End
```

Verified against a real local MySQL 8.0 instance while this project was generated.
