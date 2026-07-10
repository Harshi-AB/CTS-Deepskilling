# Exercise 6 - Criteria Query (and Skill)

Demonstrates the JPA Criteria API (`CriteriaBuilder`, `CriteriaQuery`,
`Root`, `Predicate`, `TypedQuery`) for building a WHERE clause **dynamically
at runtime**, based on whichever filters the caller actually supplied -
exactly the "user searches for a laptop and ticks some filters on the left
panel" scenario described in the document.

Two independent demonstrations are included:

1. **`ProductRepository.search(ProductSearchCriteria)`** - the retail laptop
   scenario itself: customer review, hard disk size, RAM size, CPU speed,
   operating system, weight, CPU. Only filters that are non-null in the
   `ProductSearchCriteria` object get turned into a `Predicate`; unset
   filters are simply skipped, so the WHERE clause naturally adapts to
   whatever the user picked - no hand-written string concatenation needed.

2. **`EmployeeCriteriaRepository.search(EmployeeSearchCriteria)`** - fulfils
   the "...and Skill" part of the exercise title: filters Employees by
   department, employment type, minimum salary, **and** by whether they
   have a specific `Skill`, using a Criteria API `Join` across the
   many-to-many `Employee.skillList` association introduced in Exercise 2.

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

## Expected output (abridged, based on sql/schema.sql sample data)

```
[..] INFO OrmLearnApplication - Start - testProductCriteriaSearch
Hibernate: select product0_.product_id as ... from product product0_ where product0_.ram_size_gb>=8 and product0_.operating_system=?
[..] DEBUG OrmLearnApplication - Matched product -> Product{id=1, name='AlphaBook Air 14', ..., os='Windows 11', ...}
[..] DEBUG OrmLearnApplication - Matched product -> Product{id=2, name='ProNote X1', ..., os='Windows 11', ...}
[..] INFO OrmLearnApplication - End - testProductCriteriaSearch
[..] INFO OrmLearnApplication - Start - testEmployeeCriteriaSearchBySkill
Hibernate: select distinct employee0_.em_id as ... from employee employee0_ inner join employee_skill ... inner join skill ... where employee0_.em_permanent=1 and skill1_.sk_name=?
[..] DEBUG OrmLearnApplication - Matched employee -> Employee{id=1, name='Arun Kumar', ..., permanent=true, department=Engineering}
[..] DEBUG OrmLearnApplication - Matched employee -> Employee{id=6, name='Farida Sheikh', ..., permanent=true, department=Finance}
[..] INFO OrmLearnApplication - End - testEmployeeCriteriaSearchBySkill
```
