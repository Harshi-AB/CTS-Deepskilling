# Exercise 08 — Creating Projections

## Folder structure
```
Exercise-08-Employee Management System - Creating Projections/
├── (all files from Ex.07)
├── EmployeeSummary.java        <-- new (closed projection interface)
├── EmployeeContactInfo.java    <-- new (closed projection interface)
├── ProjectionFactory.java      <-- new
├── EmployeeRepository.java     <-- extended with dynamic-projection methods
├── RepositoryFactory.java      <-- extended to wrap results in projections
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
=== Exercise 08: Creating Projections ===

EmployeeSummary projection (empId, name, salary only):
  #1 Harshitha R - 75000
  #2 Arun Kumar - 60000

EmployeeContactInfo projection (name, email only) for salary > 50000:
  Harshitha R <harshitha@example.com>
  Arun Kumar <arun@example.com>
```
