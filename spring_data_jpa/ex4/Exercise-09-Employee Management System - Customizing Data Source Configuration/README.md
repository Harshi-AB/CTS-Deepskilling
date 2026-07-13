# Exercise 09 — Customizing Data Source Configuration

## Folder structure
```
Exercise-09-Employee Management System - Customizing Data Source Configuration/
├── (all files from Ex.08, minus DBConfig.java which is replaced)
├── application.properties       <-- new (externalized config)
├── DataSourceProperties.java    <-- new
├── SimpleConnectionPool.java    <-- new (Object Pool + Proxy pattern)
├── DataSourceConfig.java        <-- new (Singleton)
├── DBConnectionUtil.java        <-- rewritten to use the pool
├── Main.java
├── schema.sql
└── README.md
```

## Setup
```bash
mysql -u root -p < schema.sql
```
Edit `application.properties` with your MySQL credentials/pool size.

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
=== Exercise 09: Customizing Data Source Configuration ===

Pool size from application.properties: 5
Available connections before borrowing: 5
Borrowed one connection -> available now: 4
Connection released (proxy close()) -> available again: 5

Saved through pooled connections: Employee{empId=1, name='Harshitha R', ...}
```
