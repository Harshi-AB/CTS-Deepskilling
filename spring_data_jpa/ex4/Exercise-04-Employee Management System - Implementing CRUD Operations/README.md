# Exercise 04 — Implementing CRUD Operations

## Folder structure
```
Exercise-04-Employee Management System - Implementing CRUD Operations/
├── (10 annotation files + Department.java + Employee.java, from Ex.02)
├── Repository.java, CrudRepository.java, JpaRepository.java                 (from Ex.03)
├── EmployeeRepository.java, DepartmentRepository.java                       (from Ex.03)
├── DBConfig.java, DBConnectionUtil.java                                     (from Ex.01)
├── EntityMetadataUtil.java
├── SimpleJpaRepository.java
├── RepositoryFactory.java
├── Main.java
├── schema.sql
└── README.md
```

## Setup
```bash
mysql -u root -p < schema.sql
```
Edit `DBConfig.java` with your MySQL credentials.

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
=== Exercise 04: Implementing CRUD Operations ===

Saved department: Department{deptId=1, deptName='Engineering', location='Bangalore'}
Saved employee: Employee{empId=1, name='Harshitha R', email='harshitha@example.com', salary=75000.00, dateOfJoining=2026-07-13, active=true, department=Engineering}

findById(1): Employee{empId=1, name='Harshitha R', ...}
existsById: true
count: 1

After update: Employee{empId=1, ..., salary=82000.00, ...}

findAll() -> 1 employee(s)
  Employee{empId=1, ...}

Deleted employee 1 -> now exists? false
```
