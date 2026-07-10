# Exercise 5 - Get all employees using Native Query

Demonstrates a Native Query - plain SQL sent directly to MySQL instead of
HQL/JPQL:

```
SELECT * FROM employee
```

Equivalent Spring Data JPA declaration:
```java
@Query(value = "SELECT * FROM employee", nativeQuery = true)
List<Employee> getAllEmployeesNative();
```

**Note:** native queries should be kept to a minimum - they are tied to the
specific SQL dialect of the underlying database (MySQL here) and reduce
portability. Prefer HQL/JPQL unless a query genuinely cannot be expressed
that way.

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

## Expected output (abridged)

```
[..] INFO OrmLearnApplication - Start - testGetAllEmployeesNative
Hibernate: SELECT * FROM employee
[..] DEBUG OrmLearnApplication - Employee (native):Employee{id=1, name='Arun Kumar', salary=65000.0, permanent=true, department=null}
[..] DEBUG OrmLearnApplication - Employee (native):Employee{id=2, name='Bhavana Reddy', salary=72000.0, permanent=true, department=null}
[..] DEBUG OrmLearnApplication - Employee (native):Employee{id=3, name='Charles Mathew', salary=45000.0, permanent=false, department=null}
...
[..] INFO OrmLearnApplication - End - testGetAllEmployeesNative
```

(`department` prints as `null` in `toString()` because it is a LAZY
association and this native query does not join/fetch it - accessing
`getDepartment().getName()` later, outside an open EntityManager, would
trigger a lazy-init exception. This is expected native-query behaviour.)
