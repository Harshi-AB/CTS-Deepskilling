# Exercise 6 — Find a country based on country code

## What this demonstrates
Builds on Exercise 5 (full 249-country dataset loaded at startup) by adding
`CountryService.findCountryByCode(String)`, which looks the country up via
`countryRepository.findById(code)` and throws a checked
`CountryNotFoundException` if no row exists for that code — exactly as
described in Hands-on 6.

## Folder structure
```
Exercise-06-Find a country based on country code/
├── sql/
│   └── countries.sql
├── src/
│   ├── (same mini Spring/JPA framework files as Exercise 5)
│   ├── CountryNotFoundException.java   (new)
│   ├── Country.java
│   ├── CountryDataLoader.java
│   ├── CountryRepository.java
│   ├── CountryService.java             (adds findCountryByCode())
│   └── OrmLearnApplication.java        (adds getAllCountriesTest())
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

## Expected output (last few lines — full 249-country load precedes this,
see expected_output_full.txt)
```
... INFO OrmLearnApplication  Start
Hibernate: select * from country where id=? [pk=IN]
... DEBUG OrmLearnApplication  Country:Country [code=IN, name=India]
... INFO OrmLearnApplication  End
```

## To run against a real MySQL database instead
Same as Exercise 5 — execute `sql/countries.sql`, add `mysql-connector-j`,
and back `MySQLDatabase.findById()` with a real
`SELECT * FROM country WHERE co_code = ?` prepared statement.
