# Exercise 03 — Creating Repositories

## Folder structure
```
Exercise-03-Employee Management System - Creating Repositories/
├── Entity.java, Table.java, Id.java, GeneratedValue.java, GenerationType.java,
│   Column.java, Transient.java, ManyToOne.java, JoinColumn.java, OneToMany.java   (annotations, from Ex.02)
├── Department.java, Employee.java                                                (entities, from Ex.02)
├── Repository.java
├── CrudRepository.java
├── JpaRepository.java
├── EmployeeRepository.java
├── DepartmentRepository.java
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
(Purely structural — no DB connection is opened; this exercise is about the
repository *interface hierarchy*, not runtime behaviour.)

## Expected output
```
=== Exercise 03: Creating Repositories ===

EmployeeRepository interface hierarchy:
  extends JpaRepository
    extends CrudRepository

Inherited method contract available on EmployeeRepository:
  long count(...)
  void delete(...)
  Object save(...)
  List findAll(...)
  Optional findById(...)
  boolean existsById(...)
  List findAllById(...)
  void deleteById(...)
  void deleteAll(...)

DepartmentRepository also compiles against the same generic hierarchy: DepartmentRepository
```
