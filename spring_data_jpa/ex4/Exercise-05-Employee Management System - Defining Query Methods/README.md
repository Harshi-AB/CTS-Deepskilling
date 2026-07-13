# Exercise 05 — Defining Query Methods

## Folder structure
```
Exercise-05-Employee Management System - Defining Query Methods/
├── (all files from Ex.04)
├── DerivedQueryExecutor.java     <-- new
├── EmployeeRepository.java       <-- extended with derived query methods
├── RepositoryFactory.java        <-- extended with query-method fallback
├── Main.java
├── schema.sql
└── README.md
```

## Setup
```bash
mysql -u root -p < schema.sql
```

## Compilation command
```bash
javac *.java
```

## Run command
```bash
java -cp .:mysql-connector-j-8.x.x.jar Main
```

## Expected output
```
=== Exercise 05: Defining Query Methods ===

Active employees: [Employee{...Arun Kumar...}, Employee{...Divya Sree...}]

Name contains 'Sree': [Employee{...Divya Sree...}]

Salary > 50000: [Employee{...Arun Kumar...}, Employee{...Divya Sree...}]

Active AND salary > 50000: [Employee{...Arun Kumar...}, Employee{...Divya Sree...}]

All ordered by salary desc: [Employee{...Divya Sree...}, Employee{...Arun Kumar...}, Employee{...Karthik R...}]

countByActive(true): 2
existsByEmail('arun@example.com'): true
```
