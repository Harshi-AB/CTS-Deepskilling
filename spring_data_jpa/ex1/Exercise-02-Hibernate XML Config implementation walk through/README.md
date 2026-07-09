# Exercise 2 — Hibernate XML Config implementation walk through

## What this demonstrates
SME walkthrough of classic Hibernate XML Configuration
(https://www.tutorialspoint.com/hibernate/hibernate_examples.htm), rebuilt as
runnable Core Java. `config/hibernate.cfg.xml` and `config/Employee.hbm.xml`
are **real** Hibernate-format XML files, parsed at runtime with plain DOM
(`javax.xml.parsers` — part of core Java, no external XML jar). `Employee.java`
is a plain POJO with **no annotations** — its object-relational mapping lives
entirely in `Employee.hbm.xml`, exactly as classic Hibernate XML config works.

Covers every explanation topic from Hands-on 2:
`SessionFactory`, `Session`, `Transaction`, `beginTransaction()`, `commit()`,
`rollback()`, `session.save()`, `session.createQuery().list()`, `session.get()`,
`session.delete()`.

## Folder structure
```
Exercise-02-Hibernate XML Config implementation walk through/
├── config/
│   ├── hibernate.cfg.xml         (real Hibernate connection config)
│   └── Employee.hbm.xml          (real Hibernate O/R mapping file)
├── src/
│   ├── Configuration.java        (~ org.hibernate.cfg.Configuration)
│   ├── ConnectionConfig.java     (parsed connection settings holder)
│   ├── Employee.java             (plain POJO, no annotations)
│   ├── EntityMapping.java        (parsed mapping holder)
│   ├── HibernateException.java
│   ├── HibernateMappingReader.java   (DOM parser for .hbm.xml)
│   ├── HibernateXmlConfigReader.java (DOM parser for .cfg.xml)
│   ├── Logger.java / LoggerFactory.java
│   ├── ManageEmployee.java       (main class - CRUD walkthrough)
│   ├── MySQLDatabase.java        (in-memory MySQL simulator)
│   ├── Query.java                (~ org.hibernate.query.Query)
│   ├── Session.java              (~ org.hibernate.Session)
│   ├── SessionEntityMapper.java  (reflection: entity <-> row mapping)
│   ├── SessionFactory.java       (~ org.hibernate.SessionFactory)
│   └── Transaction.java          (~ org.hibernate.Transaction)
└── expected_output.txt
```

## Compilation command
Run from inside `Exercise-02-Hibernate XML Config implementation walk through/`:
```
javac -d out src/*.java
```

## Run command
Must be run from this exercise's root folder (so the relative path
`config/hibernate.cfg.xml` resolves):
```
java -cp out ManageEmployee
```

## Expected output (abridged — see expected_output.txt for the full run)
```
... INFO SessionFactory  Building SessionFactory for dialect=org.hibernate.dialect.MySQL8Dialect
... INFO SessionFactory  Connecting to url=jdbc:mysql://localhost:3306/hibernatelearn as user=root
... INFO SessionFactory  Mapped entity Employee -> table EMPLOYEE
... DEBUG Session        Beginning transaction
Hibernate: insert into EMPLOYEE (id, first_name, last_name, salary) values ('1001', 'Zara', 'Ali', '1000')
... DEBUG Transaction    Committing transaction
... INFO ManageEmployee  Employee created with id=1001
...
Hibernate: select * from EMPLOYEE
... INFO ManageEmployee  Employee: Employee [id=1001, firstName=Zara, lastName=Ali, salary=1000]
... INFO ManageEmployee  Employee: Employee [id=1002, firstName=Daisy, lastName=Das, salary=5000]
... INFO ManageEmployee  Employee: Employee [id=1003, firstName=John, lastName=Paul, salary=10000]
...
Hibernate: update EMPLOYEE set id='1001', first_name='Zara', last_name='Ali', salary='5000' where id=?
... INFO ManageEmployee  Employee id=1001 updated to salary=5000
...
Hibernate: delete from EMPLOYEE where id=? [pk=1002]
... INFO ManageEmployee  Employee id=1002 deleted
...
... INFO SessionFactory  SessionFactory closed
```

## To run against a real MySQL database instead
Add `mysql-connector-j` to the classpath and rewrite `Session`'s save/get/
delete methods to issue real `PreparedStatement` calls using the JDBC URL,
username, and password already parsed out of `config/hibernate.cfg.xml`.
