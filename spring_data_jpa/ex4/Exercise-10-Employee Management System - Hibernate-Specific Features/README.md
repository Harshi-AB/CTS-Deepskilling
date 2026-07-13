# Exercise 10 — Hibernate-Specific Features

## Folder structure
```
Exercise-10-Employee Management System - Hibernate-Specific Features/
├── (all files from Ex.09)
├── SecondLevelCache.java      <-- new
├── Session.java                <-- new (L1 cache + dirty-checking / dynamic UPDATE)
├── BatchInsertProcessor.java   <-- new (JDBC batch inserts)
├── Main.java
├── application.properties
├── schema.sql
└── README.md
```

## Setup
```bash
mysql -u root -p < schema.sql
```
Edit `application.properties` with your MySQL credentials.

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
=== Exercise 10: Hibernate-Specific Features ===

1) Batch inserting 5 employees in a single round trip...
   Batch executed, rows affected per statement: 5

2) First-level cache (same Session, same id, loaded twice):
  [CACHE MISS -> querying MySQL]  Employee#1
  [L1 cache HIT]  Employee#1

3) Second-level cache (new Session, same id):
  [L2 cache HIT]  Employee#1

4) Dynamic update - changing only the salary field:
  [L1 cache HIT]  Employee#1
  [dynamic update SQL] UPDATE employee SET salary = ? WHERE emp_id = ?   changed=[salary]

   Calling update() again with NO changes:
  [dirty check] no changed fields -> UPDATE skipped entirely
```
