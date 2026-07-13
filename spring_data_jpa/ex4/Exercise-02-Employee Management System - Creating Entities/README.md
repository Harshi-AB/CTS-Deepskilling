# Exercise 02 — Creating Entities

## Folder structure
```
Exercise-02-Employee Management System - Creating Entities/
├── Entity.java
├── Table.java
├── Id.java
├── GeneratedValue.java
├── GenerationType.java
├── Column.java
├── Transient.java
├── ManyToOne.java
├── JoinColumn.java
├── OneToMany.java
├── Department.java
├── Employee.java
├── Main.java
├── schema.sql
└── README.md
```

## Compilation command
```bash
javac *.java
```

## Run command
```bash
java Main
```
(No DB connection needed for this exercise — it only demonstrates entity
metadata via reflection. `schema.sql` is provided for reference / to run
ahead of Exercise 03 onward.)

## Expected output
```
=== Exercise 02: Creating Entities ===

Created entity instance: Employee{empId=0, name='Harshitha R', email='harshitha@example.com', salary=75000.00, dateOfJoining=2026-07-13, active=true, department=Engineering}

Reflecting over Employee.class annotations:
  @Entity present:  true
  @Table name:       employee
  field 'empId' -> column 'emp_id' (nullable=true, unique=false)
  field 'name' -> column 'name' (nullable=false, unique=false)
  field 'email' -> column 'email' (nullable=true, unique=true)
  field 'salary' -> column 'salary' (nullable=true, unique=false)
  field 'dateOfJoining' -> column 'date_of_joining' (nullable=true, unique=false)
  field 'active' -> column 'active' (nullable=true, unique=false)
  field 'department' -> @ManyToOne, join column 'dept_id'
```
