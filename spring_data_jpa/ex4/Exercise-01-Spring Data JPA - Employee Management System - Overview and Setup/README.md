# Exercise 01 — Overview and Setup

## Folder structure
```
Exercise-01-Spring Data JPA - Employee Management System - Overview and Setup/
├── DBConfig.java
├── DBConnectionUtil.java
├── Main.java
├── schema.sql
└── README.md
```

## Setup
```bash
mysql -u root -p < schema.sql
```
Edit `DBConfig.java` with your MySQL URL/username/password if different from the defaults.

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
=== Spring Data JPA Simulation - Exercise 01: Overview and Setup ===
Connection established successfully with employee_db!

Rows currently in connection_test:
  id=1 | message=Setup verified | created_at=2026-07-13 09:55:01.0
```
