# Exercise 06 - Many-to-Many: Employee <-> Skill (LAZY vs EAGER)

## Folder structure
```
Exercise-06-Implement many to many relationship between Employee and Skill/
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
    ├── Department.java            -- @Entity (employee's many-to-one target)
    ├── Skill.java                 -- @Entity, @ManyToMany(mappedBy = "skillList") - inverse side
    ├── Employee.java              -- @Entity, owning side, @ManyToMany(fetch=LAZY, default) + @JoinTable
    ├── EmployeeEager.java         -- same table, @ManyToMany(fetch = EAGER)
    ├── DepartmentRepository.java, SkillRepository.java, EmployeeRepository.java,
    │   EmployeeEagerRepository.java
    ├── DepartmentService is not needed here; EmployeeService.java, EmployeeEagerService.java,
    │   SkillService.java
    └── OrmLearnApplication.java   -- testGetEmployee() / testGetEmployeeEager() /
                                       testAddSkillToEmployee()
```

## The LAZY -> EAGER exercise, exactly as documented
1. `Employee` owns the association: `skillList` carries `@ManyToMany` +
   `@JoinTable(name = "employee_skill", joinColumns = @JoinColumn(name = "es_em_id"),
   inverseJoinColumns = @JoinColumn(name = "es_sk_id"))`, with no explicit
   `fetch` - defaulting to `FetchType.LAZY`. `testGetEmployee()` loads
   employee id 1 and calling `getSkillList()` throws
   `LazyInitializationException`, exactly as the hands-on describes.
2. `EmployeeEager` maps the same table but declares
   `@ManyToMany(fetch = FetchType.EAGER)`. `testGetEmployeeEager()` shows
   the skill list already populated.
3. `testAddSkillToEmployee()` then assigns skill id 4 (Project Management)
   to employee id 3 (Carol White) - a pairing that doesn't exist in the
   seed data - and writes the new row into the `employee_skill` join
   table directly (the mini-ORM's generic `save()` persists an entity's
   own columns only; join-table writes for `@ManyToMany` are issued
   explicitly, as noted in the code comments).

`Skill` is the inverse side and declares `@ManyToMany(mappedBy = "skillList")`.

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
[INFO ] EmployeeService - Start
[INFO ] EmployeeService - End
[DEBUG] OrmLearnApplication - Employee:Employee{id=1, name='Alice Johnson', salary=75000.0, permanent=true, dateOfBirth=1990-05-10}
[DEBUG] OrmLearnApplication - Department:Department{id=1, name='Engineering'}
Caught expected LazyInitializationException: failed to lazily initialize a collection: employee.skillList, could not initialize proxy - no session
[INFO ] OrmLearnApplication - End
[INFO ] OrmLearnApplication - Start
[INFO ] EmployeeEagerService - Start
[INFO ] EmployeeEagerService - End
[DEBUG] OrmLearnApplication - Employee:Employee{id=1, name='Alice Johnson', salary=75000.0, permanent=true, dateOfBirth=1990-05-10}
[DEBUG] OrmLearnApplication - Skills:[Skill{id=1, name='Java'}, Skill{id=2, name='SQL'}]
[INFO ] OrmLearnApplication - End
[INFO ] OrmLearnApplication - Start
[INFO ] EmployeeEagerService - Start
[INFO ] EmployeeEagerService - End
[INFO ] SkillService - Start
[INFO ] SkillService - End
[INFO ] EmployeeEagerService - Start
[INFO ] EmployeeEagerService - End
[DEBUG] OrmLearnApplication - Employee skills after update:[Skill{id=3, name='Communication'}, Skill{id=4, name='Project Management'}]
[INFO ] OrmLearnApplication - End
```

Verified against a real local MySQL 8.0 instance while this project was generated.
