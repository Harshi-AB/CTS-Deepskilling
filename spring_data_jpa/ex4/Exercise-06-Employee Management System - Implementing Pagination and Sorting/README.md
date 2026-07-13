# Exercise 06 — Implementing Pagination and Sorting

## Folder structure
```
Exercise-06-Employee Management System - Implementing Pagination and Sorting/
├── (all files from Ex.05)
├── Sort.java          <-- new
├── Pageable.java       <-- new
├── PageRequest.java    <-- new
├── Page.java           <-- new
├── JpaRepository.java  <-- extended with findAll(Sort) / findAll(Pageable)
├── SimpleJpaRepository.java  <-- implements both new methods
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
=== Exercise 06: Implementing Pagination and Sorting ===

All employees sorted by salary DESC:
  Priya - 75000
  Suresh - 68000
  Meena - 61000
  Karthik - 54000
  Divya - 47000
  Arun - 40000

Page 1 (size 2), sorted by name ASC:
Page 1/3 (size=2, totalElements=6) -> [Employee{...Arun...}, Employee{...Divya...}]

Page 2 (size 2), sorted by name ASC:
Page 2/3 (size=2, totalElements=6) -> [Employee{...Karthik...}, Employee{...Meena...}]
```
