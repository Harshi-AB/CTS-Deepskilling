# Exercise 5 — Implement services for managing Country

## What this demonstrates
Continues the `orm-learn` application from Exercise 1. Per Hands-on 5:
- **ddl-auto** explanation: `create` drops+recreates, `validate` checks
  schema exists and throws if not, `update` adds missing tables/columns,
  `create-drop` drops the table after the run. `sql/countries.sql` is the
  real MySQL script; running it against MySQL directly reflects `create`/
  `update` behaviour, since it drops and recreates the table before seeding.
- **Population**: all sample rows are cleared and replaced with the full
  list of 249 countries.

`CountryDataLoader.java` reads `sql/countries.sql` line by line with a plain
regex (`java.util.regex` + `java.io` — core Java only, no JDBC jar) and calls
`countryRepository.save()` for every row, so the seed data flows through the
exact same repository code production calls would use.

## Folder structure
```
Exercise-05-Implement services for managing Country/
├── sql/
│   └── countries.sql              (real MySQL schema + all 249 countries)
├── src/
│   ├── ApplicationContext.java, Autowired.java, Column.java, Entity.java,
│   │   GeneratedValue.java, Id.java, JpaRepository.java,
│   │   JpaRepositoryProxyFactory.java, Repository.java, Service.java,
│   │   SpringApplication.java, SpringBootApplication.java, Table.java,
│   │   Transactional.java                              (mini Spring/JPA framework)
│   ├── Logger.java / LoggerFactory.java / MySQLDatabase.java
│   ├── Country.java
│   ├── CountryDataLoader.java      (reads sql/countries.sql)
│   ├── CountryRepository.java
│   ├── CountryService.java         (getAllCountries())
│   └── OrmLearnApplication.java    (main class)
└── expected_output_full.txt
```

## Compilation command
```
javac -d out -encoding UTF-8 src/*.java
```
(`-encoding UTF-8` matters because the country list includes non-ASCII names
like "Åland Islands" and "Côte d'Ivoire".)

## Run command
Must be run from this exercise's root folder (so `sql/countries.sql` resolves),
and needs `-Dstdout.encoding=UTF-8` so those non-ASCII names print correctly:
```
java -Dstdout.encoding=UTF-8 -cp out OrmLearnApplication
```

## Expected output (abridged — see expected_output_full.txt for the full run)
```
... INFO OrmLearnApplication  Inside main
Hibernate: insert into country (co_code, co_name) values ('AF', 'Afghanistan')
Hibernate: insert into country (co_code, co_name) values ('AL', 'Albania')
... (249 insert lines total) ...
Hibernate: insert into country (co_code, co_name) values ('AX', 'Åland Islands')
... INFO OrmLearnApplication  Loaded 249 countries from sql/countries.sql
... INFO OrmLearnApplication  Start
Hibernate: select * from country
... DEBUG OrmLearnApplication  countries.size()=249
... DEBUG OrmLearnApplication  first 3 countries=[Country [code=AF, name=Afghanistan], Country [code=AL, name=Albania], Country [code=DZ, name=Algeria]]
... DEBUG OrmLearnApplication  last 3 countries=[Country [code=ZM, name=Zambia], Country [code=ZW, name=Zimbabwe], Country [code=AX, name=Åland Islands]]
... INFO OrmLearnApplication  End
```

## To run against a real MySQL database instead
Execute `sql/countries.sql` directly in MySQL Workbench / the `mysql` client
(it includes `create schema`, `drop table if exists`, `create table`, and all
249 `insert` statements), add `mysql-connector-j` to the classpath, and swap
`MySQLDatabase` for real JDBC calls.
