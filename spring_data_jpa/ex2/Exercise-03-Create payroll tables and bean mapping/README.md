# Exercise 03 - Payroll tables and bean mapping

## Folder structure
```
Exercise-03-Create payroll tables and bean mapping/
├── README.md
├── sql/
│   └── payroll.sql   -- creates department, skill, employee, employee_skill + seed data
├── lib/              -- put mysql-connector-j-<version>.jar here
└── src/
    ├── Entity.java, Table.java, Id.java, Column.java, GeneratedValue.java,
    │   GenerationType.java, Transient.java, ManyToOne.java, OneToMany.java,
    │   ManyToMany.java, JoinColumn.java, JoinTable.java, FetchType.java,
    │   Service.java, Transactional.java     -- hand-written JPA-style annotations
    ├── Repository.java, RepositoryFactory.java, RepositoryInvocationHandler.java,
    │   QueryMethodParser.java, EntityUtils.java, DBConnection.java,
    │   LazyInitializationException.java, Logger.java  -- shared mini-ORM engine
    ├── Employee.java              -- @Entity, plain fields only (no relations yet)
    ├── Department.java            -- @Entity, plain fields only (no relations yet)
    ├── Skill.java                 -- @Entity, plain fields only (no relations yet)
    ├── EmployeeRepository.java
    ├── DepartmentRepository.java
    ├── SkillRepository.java
    └── OrmLearnApplication.java   -- main() driver, loads and prints every table
```

This exercise sets up the payroll schema and the three bean-mapped entity
classes (`Employee`, `Department`, `Skill`) with `@Entity` / `@Table` /
`@Id` / `@Column` annotations, getters/setters and `toString()` -
relationships between them are added in Exercises 04-06.

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
[INFO ] OrmLearnApplication - Start - testListDepartments
Department{id=1, name='Engineering'}
Department{id=2, name='Human Resources'}
Department{id=3, name='Finance'}
[INFO ] OrmLearnApplication - End - testListDepartments
[INFO ] OrmLearnApplication - Start - testListSkills
Skill{id=1, name='Java'}
Skill{id=2, name='SQL'}
Skill{id=3, name='Communication'}
Skill{id=4, name='Project Management'}
[INFO ] OrmLearnApplication - End - testListSkills
[INFO ] OrmLearnApplication - Start - testListEmployees
Employee{id=1, name='Alice Johnson', salary=75000.0, permanent=true, dateOfBirth=1990-05-10}
Employee{id=2, name='Bob Smith', salary=65000.0, permanent=false, dateOfBirth=1992-08-21}
Employee{id=3, name='Carol White', salary=58000.0, permanent=true, dateOfBirth=1988-02-14}
Employee{id=4, name='David Brown', salary=72000.0, permanent=true, dateOfBirth=1985-11-30}
[INFO ] OrmLearnApplication - End - testListEmployees
```

Verified against a real local MySQL 8.0 instance while this project was generated.
