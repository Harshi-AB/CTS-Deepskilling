# Exercise 3 — Hibernate Annotation Config implementation walk through

## What this demonstrates
SME walkthrough of classic Hibernate Annotation Configuration
(https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm). Same
CRUD lifecycle as Exercise 2, but `Employee.java` now carries `@Entity`,
`@Table`, `@Id`, `@GeneratedValue`, `@Column` directly — there is **no**
`Employee.hbm.xml` mapping file. `AnnotationEntityScanner` builds the same
`EntityMapping` object Exercise 2 builds from XML, but by reading annotations
via reflection instead — exactly what Hibernate's
`AnnotationConfiguration.addAnnotatedClass()` does internally.

`config/hibernate.cfg.xml` still supplies the dialect, driver class,
connection URL, username, and password — only the *mapping* moved from XML to
annotations.

## Folder structure
```
Exercise-03-Hibernate Annotation Config implementation walk through/
├── config/
│   └── hibernate.cfg.xml           (connection settings only — no <mapping/>)
├── src/
│   ├── AnnotationConfiguration.java (~ org.hibernate.cfg.AnnotationConfiguration)
│   ├── AnnotationEntityScanner.java (reflection: @Entity -> EntityMapping)
│   ├── Column.java / Entity.java / GeneratedValue.java / Id.java / Table.java
│   ├── ConnectionConfig.java
│   ├── Employee.java                (annotated persistence class)
│   ├── EntityMapping.java
│   ├── HibernateException.java
│   ├── HibernateXmlConfigReader.java
│   ├── Logger.java / LoggerFactory.java
│   ├── ManageEmployeeAnnotation.java (main class - CRUD walkthrough)
│   ├── MySQLDatabase.java
│   ├── Query.java
│   ├── Session.java
│   ├── SessionEntityMapper.java
│   ├── SessionFactory.java
│   └── Transaction.java
└── expected_output.txt
```

## Compilation command
```
javac -d out src/*.java
```

## Run command
Must be run from this exercise's root folder:
```
java -cp out ManageEmployeeAnnotation
```

## Expected output (abridged — see expected_output.txt for the full run)
```
... INFO SessionFactory  Building SessionFactory for dialect=org.hibernate.dialect.MySQL8Dialect
... INFO SessionFactory  Mapped entity Employee -> table EMPLOYEE
Hibernate: insert into EMPLOYEE (id, first_name, last_name, salary) values ('1001', 'Zara', 'Ali', '1000')
... INFO ManageEmployeeAnnotation  Employee created with id=1001
...
Hibernate: select * from EMPLOYEE
... INFO ManageEmployeeAnnotation  Employee: Employee [id=1001, firstName=Zara, lastName=Ali, salary=1000]
... INFO ManageEmployeeAnnotation  Employee: Employee [id=1002, firstName=Daisy, lastName=Das, salary=5000]
... INFO ManageEmployeeAnnotation  Employee: Employee [id=1003, firstName=John, lastName=Paul, salary=10000]
...
Hibernate: update EMPLOYEE set id='1001', first_name='Zara', last_name='Ali', salary='7000' where id=?
... INFO ManageEmployeeAnnotation  Employee id=1001 updated to salary=7000
...
Hibernate: delete from EMPLOYEE where id=? [pk=1002]
... INFO ManageEmployeeAnnotation  Employee id=1002 deleted
...
... INFO SessionFactory  SessionFactory closed
```

## To run against a real MySQL database instead
Add `mysql-connector-j` to the classpath and connect using the credentials in
`config/hibernate.cfg.xml`; `AnnotationEntityScanner`'s reflection-derived
table/column mapping already matches what Hibernate would generate.
