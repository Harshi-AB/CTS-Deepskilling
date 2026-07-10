# Exercise 2 - Get all permanent employees using HQL

Retrieves all permanent employees (`em_permanent = 1`) together with their
department and skill list, using a single optimised HQL query with
`left join fetch`.

```
SELECT e FROM Employee e
left join fetch e.department d
left join fetch e.skillList
WHERE e.permanent = true
```

`fetch` is what actually populates the associated Department/Skill objects
in a single SQL round trip - a plain `join` (without `fetch`) only affects
the generated SQL's join clause and does NOT populate the Java object graph.

## 1. Setup MySQL schema

```bash
mysql -u root -p < sql/schema.sql
```

## 2. Download dependency jars

See `lib/README.txt`. Place all jars inside `lib/`.

## 3. Update DB credentials

Edit `META-INF/persistence.xml` if your MySQL user/password differ from
`root` / `root`.

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
[..] INFO OrmLearnApplication - Start
Hibernate:
    select employee0_.em_id as em_id1_2_0_, department1_.dp_id as dp_id1_0_1_,
           skill3_.sk_id as sk_id1_3_2_, ...
    from employee employee0_
    left outer join department department1_ on employee0_.em_dp_id=department1_.dp_id
    left outer join employee_skill skilllist2_ on employee0_.em_id=skilllist2_.es_em_id
    left outer join skill skill3_ on skilllist2_.es_sk_id=skill3_.sk_id
    where employee0_.em_permanent=1
[..] DEBUG OrmLearnApplication - Permanent Employees:[Employee{id=1, name='Arun Kumar', ..., department=Engineering}, ...]
[..] DEBUG OrmLearnApplication - Skills:[Skill{id=1, name='Java'}, Skill{id=3, name='Spring Data JPA'}, Skill{id=4, name='Hibernate'}]
[..] DEBUG OrmLearnApplication - Skills:[Skill{id=2, name='SQL'}, Skill{id=5, name='MySQL Administration'}]
[..] DEBUG OrmLearnApplication - Skills:[]
[..] DEBUG OrmLearnApplication - Skills:[Skill{id=1, name='Java'}, Skill{id=2, name='SQL'}]
[..] INFO OrmLearnApplication - End
```

Notice only **one** SQL statement is executed - confirming the eager N+1
problem described in the document has been eliminated by using
`left join fetch`.
