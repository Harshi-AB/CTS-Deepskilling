# Exercise 8 — Update a country based on code

## What this demonstrates
Builds on Exercise 7 by adding `CountryService.updateCountry(String code,
String name)`, which looks the row up first (throwing
`CountryNotFoundException` if missing), mutates the managed `Country`
object's name, and saves it back — the standard Hibernate/JPA
"load-modify-save" update pattern.

## Folder structure
```
Exercise-08-Update a country based on code/
├── sql/
│   └── countries.sql
├── src/
│   ├── (same mini Spring/JPA framework files + CountryNotFoundException.java)
│   ├── Country.java
│   ├── CountryDataLoader.java
│   ├── CountryRepository.java
│   ├── CountryService.java             (adds updateCountry())
│   └── OrmLearnApplication.java        (adds testUpdateCountry())
└── expected_output_full.txt
```

## Compilation command
```
javac -d out -encoding UTF-8 src/*.java
```

## Run command
```
java -Dstdout.encoding=UTF-8 -cp out OrmLearnApplication
```

## Expected output (last few lines — full 249-country load + Exercises 6/7's
tests precede this, see expected_output_full.txt)
```
... INFO OrmLearnApplication  Start
Hibernate: select * from country where id=? [pk=ZZ]
Hibernate: update country set co_code='ZZ', co_name='New Zephyrland' where id=?
Hibernate: select * from country where id=? [pk=ZZ]
... DEBUG OrmLearnApplication  Country after update: Country [code=ZZ, name=New Zephyrland]
... INFO OrmLearnApplication  End
```

## To run against a real MySQL database instead
Same as previous exercises — back `MySQLDatabase.save()` (called again after
mutation) with a real `UPDATE country SET co_name = ? WHERE co_code = ?`
prepared statement.
