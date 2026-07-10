# Exercise 1 - Introduction to HQL and JPQL

Demonstrates the relationship between HQL (Hibernate Query Language) and
JPQL (Java Persistence Query Language):

* JPQL is a strict subset of HQL - every JPQL query is valid HQL, not vice versa.
* `getAllEmployeesJPQL()` uses the strict JPQL form: `SELECT e FROM Employee e`
* `getAllEmployeesHQL()` uses an HQL-only shorthand: `FROM Employee` (no SELECT/alias)
* `copyEmployeeUsingHQLInsert()` uses the HQL-only bulk `INSERT INTO ... SELECT ...`
  statement - JPQL has no INSERT statement at all (only bulk UPDATE/DELETE).

## 1. Setup MySQL schema

```bash
mysql -u root -p < sql/schema.sql
```

## 2. Download dependency jars

See `lib/README.txt` for the full jar list and download commands. Place all
jars directly inside the `lib/` folder.

## 3. Update DB credentials

Edit `META-INF/persistence.xml` and set your MySQL username/password if they
differ from `root` / `root`.

## Compilation command

```bash
javac -cp "lib/*" *.java
```

## Run command

```bash
java -cp ".:lib/*" OrmLearnApplication
```
(Windows: `java -cp ".;lib/*" OrmLearnApplication`)

## Expected output (abridged)

```
[..] INFO OrmLearnApplication - Start - testJPQLQuery (JPQL: SELECT e FROM Employee e)
Hibernate: select employee0_.em_id ... from employee employee0_
[..] DEBUG OrmLearnApplication - JPQL result -> Employee{id=1, name='Arun Kumar', ...}
...
[..] INFO OrmLearnApplication - End - testJPQLQuery
[..] INFO OrmLearnApplication - Start - testHQLQuery (HQL shorthand: FROM Employee)
Hibernate: select employee0_.em_id ... from employee employee0_
[..] DEBUG OrmLearnApplication - HQL result -> Employee{id=1, name='Arun Kumar', ...}
...
[..] INFO OrmLearnApplication - End - testHQLQuery
[..] INFO OrmLearnApplication - Start - testHQLOnlyInsert (HQL: INSERT INTO Employee ... SELECT ...)
Hibernate: insert into employee (em_date_of_birth, em_dp_id, em_name, em_permanent, em_salary) select ...
[..] DEBUG OrmLearnApplication - Rows inserted -> 1
[..] INFO OrmLearnApplication - End - testHQLOnlyInsert
```
