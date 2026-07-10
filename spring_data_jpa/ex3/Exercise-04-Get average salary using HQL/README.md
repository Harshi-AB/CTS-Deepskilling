# Exercise 4 - Get average salary using HQL

Demonstrates HQL aggregate functions:

```
SELECT AVG(e.salary) FROM Employee e
SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id
```

`e.department.id` navigates the object association (Employee -> Department)
rather than referencing a raw foreign-key column - a key HQL/JPQL concept.
`:id` is a named parameter bound via `setParameter("id", id)` (the
`@Param`-annotation equivalent in plain JPA code). The same pattern works
for any other aggregate function (`SUM`, `MIN`, `MAX`, `COUNT`, ...).

## 1. Setup MySQL schema

```bash
mysql -u root -p < sql/schema.sql
```

## 2. Download dependency jars

See `lib/README.txt`. Place all jars inside `lib/`.

## 3. Update DB credentials

Edit `META-INF/persistence.xml` if needed.

## Compilation command

```bash
javac -cp "lib/*" *.java
```

## Run command

```bash
java -cp ".:lib/*" OrmLearnApplication
```
(Windows: `java -cp ".;lib/*" OrmLearnApplication`)

## Expected output (abridged, based on sample data in schema.sql)

```
[..] INFO OrmLearnApplication - Start - testGetAverageSalary
Hibernate: select avg(employee0_.em_salary) as col_0_0_ from employee employee0_
[..] DEBUG OrmLearnApplication - Average salary (all employees):56833.33333333333
[..] INFO OrmLearnApplication - End - testGetAverageSalary
[..] INFO OrmLearnApplication - Start - testGetAverageSalaryByDepartment
Hibernate: select avg(employee0_.em_salary) as col_0_0_ from employee employee0_ where employee0_.em_dp_id=1
[..] DEBUG OrmLearnApplication - Average salary (department id=1):59333.333333333336
[..] INFO OrmLearnApplication - End - testGetAverageSalaryByDepartment
```
