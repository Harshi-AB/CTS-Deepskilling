# Exercise 9 — Delete a country based on code

## What this demonstrates
Builds on Exercise 8 by adding `CountryService.deleteCountry(String code)`,
which delegates to `countryRepository.deleteById(code)`. The test then
confirms the deletion by asserting that `findCountryByCode()` now correctly
throws `CountryNotFoundException`.

## Folder structure
```
Exercise-09-Delete a country based on code/
├── sql/
│   └── countries.sql
├── src/
│   ├── (same mini Spring/JPA framework files + CountryNotFoundException.java)
│   ├── Country.java
│   ├── CountryDataLoader.java
│   ├── CountryRepository.java
│   ├── CountryService.java             (adds deleteCountry())
│   └── OrmLearnApplication.java        (adds testDeleteCountry())
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

## Expected output (last few lines — full 249-country load + Exercises 6/7/8's
tests precede this, see expected_output_full.txt)
```
... INFO OrmLearnApplication  Start
Hibernate: delete from country where id=? [pk=ZZ]
Hibernate: select * from country where id=? [pk=ZZ]
... DEBUG OrmLearnApplication  Confirmed deleted - lookup correctly failed: No country found for code: ZZ
... INFO OrmLearnApplication  End
```

## To run against a real MySQL database instead
Same as previous exercises — back `MySQLDatabase.deleteById()` with a real
`DELETE FROM country WHERE co_code = ?` prepared statement.

## Full CRUD lifecycle covered across Exercises 5-9
| Exercise | Method                          | Operation |
|----------|----------------------------------|-----------|
| 5        | `getAllCountries()`              | READ all  |
| 6        | `findCountryByCode(code)`        | READ one  |
| 7        | `addCountry(country)`            | CREATE    |
| 8        | `updateCountry(code, name)`      | UPDATE    |
| 9        | `deleteCountry(code)`            | DELETE    |
