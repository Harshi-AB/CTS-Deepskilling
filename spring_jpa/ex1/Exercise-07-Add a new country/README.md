# Exercise 7 — Add a new country

## What this demonstrates
Builds on Exercise 6 by adding `CountryService.addCountry(Country)`, which
simply delegates to `countryRepository.save(country)` — Spring Data JPA's
`save()` acts as an upsert, so this single call handles both inserts and
updates.

## Folder structure
```
Exercise-07-Add a new country/
├── sql/
│   └── countries.sql
├── src/
│   ├── (same mini Spring/JPA framework files + CountryNotFoundException.java)
│   ├── Country.java
│   ├── CountryDataLoader.java
│   ├── CountryRepository.java
│   ├── CountryService.java             (adds addCountry())
│   └── OrmLearnApplication.java        (adds testAddCountry())
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

## Expected output (last few lines — full 249-country load + Exercise 6's
test precede this, see expected_output_full.txt)
```
... INFO OrmLearnApplication  Start
Hibernate: insert into country (co_code, co_name) values ('ZZ', 'Zephyrland')
Hibernate: select * from country where id=? [pk=ZZ]
... DEBUG OrmLearnApplication  Newly added country found in database: Country [code=ZZ, name=Zephyrland]
... INFO OrmLearnApplication  End
```

## To run against a real MySQL database instead
Same as Exercise 5/6 — back `MySQLDatabase.save()` with a real
`INSERT ... ON DUPLICATE KEY UPDATE` (or a `SELECT` + conditional
`INSERT`/`UPDATE`) prepared statement.
