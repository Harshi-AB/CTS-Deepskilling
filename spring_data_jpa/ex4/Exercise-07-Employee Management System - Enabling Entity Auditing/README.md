# Exercise 07 — Enabling Entity Auditing

## Folder structure
```
Exercise-07-Employee Management System - Enabling Entity Auditing/
├── (all files from Ex.06)
├── CreatedDate.java              <-- new
├── LastModifiedDate.java         <-- new
├── AuditingEntityListener.java   <-- new
├── Employee.java                 <-- extended with createdAt / updatedAt
├── SimpleJpaRepository.java      <-- calls the listener on insert/update
├── Main.java
├── schema.sql                    <-- adds created_at / updated_at columns
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
=== Exercise 07: Enabling Entity Auditing ===

After insert:
  createdAt = 2026-07-13T09:55:01.123
  updatedAt = 2026-07-13T09:55:01.123

After update:
  createdAt = 2026-07-13T09:55:01.123  (unchanged)
  updatedAt = 2026-07-13T09:55:02.456  (refreshed)
```
