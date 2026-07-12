# Exercise 01 - Query Methods on the `country` table

## Folder structure
```
Exercise-01-Spring Data JPA - Write queries on country table using Query Methods/
├── README.md
├── sql/
│   ├── schema.sql          -- creates the ormlearn database and country table
│   └── country-data.sql    -- INSERTs the full 249-row ISO country list
├── lib/                    -- put mysql-connector-j-<version>.jar here (see below)
└── src/
    ├── Entity.java, Table.java, Id.java, Column.java, GeneratedValue.java,
    │   GenerationType.java, Transient.java, ManyToOne.java, OneToMany.java,
    │   ManyToMany.java, JoinColumn.java, JoinTable.java, FetchType.java,
    │   Service.java, Transactional.java        -- hand-written JPA-style annotations
    ├── Repository.java                          -- generic CRUD interface
    ├── RepositoryFactory.java                   -- creates dynamic-proxy repositories
    ├── RepositoryInvocationHandler.java          -- parses query methods -> SQL, runs JDBC
    ├── QueryMethodParser.java                    -- findByXxxContaining()... name parser
    ├── EntityUtils.java                          -- reflection helpers
    ├── DBConnection.java                         -- core JDBC connection to MySQL
    ├── LazyInitializationException.java          -- (unused in this exercise)
    ├── Logger.java                               -- minimal SLF4J-style logger
    ├── Country.java                              -- @Entity mapped to country
    ├── CountryRepository.java                    -- Query Methods
    └── OrmLearnApplication.java                  -- main() driver / test methods
```

## Why these extra classes are here
The hands-on forbids Maven/Gradle/external JARs, but the exercise is still about
**Spring Data JPA Query Methods** - i.e. deriving SQL purely from a method's name
(`findByCoNameContaining`, ...). So this project ships a small, self-contained
reimplementation of that exact mechanism: annotations + a `java.lang.reflect.Proxy`
that parses the method name and runs the equivalent JDBC/MySQL query. No Spring,
no Hibernate - only `java.sql` (bundled with the JDK) plus the MySQL driver JAR.

## Setup
1. Install MySQL and start the server.
2. Get the MySQL Connector/J JAR (the JDBC driver - this is the one unavoidable
   external file, since core Java has no MySQL driver built in) from
   https://dev.mysql.com/downloads/connector/j/ and place it in `lib/`,
   e.g. `lib/mysql-connector-j-8.4.0.jar`.
3. Load the schema and data:
   ```
   mysql -u root -p < sql/schema.sql
   mysql -u root -p ormlearn < sql/country-data.sql
   ```
4. Open `src/DBConnection.java` and update `USER` / `PASSWORD` if your MySQL
   credentials differ from `root` / `root`.

## Compilation command
```
javac -d bin src/*.java
```
(No classpath needed here - the driver JAR is only required at runtime.)

## Run command
Linux/macOS:
```
java -cp "bin:lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```
Windows:
```
java -cp "bin;lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```

## Expected output
```
[INFO ] OrmLearnApplication - Start - testSearchContaining
BV	Bouvet Island
DJ	Djibouti
GP	Guadeloupe
GS	South Georgia and the South Sandwich Islands
LU	Luxembourg
SS	South Sudan
TF	French Southern Territories
UM	United States Minor Outlying Islands
ZA	South Africa
[INFO ] OrmLearnApplication - End - testSearchContaining
[INFO ] OrmLearnApplication - Start - testSearchContainingOrdered
BV	Bouvet Island
DJ	Djibouti
TF	French Southern Territories
GP	Guadeloupe
LU	Luxembourg
ZA	South Africa
GS	South Georgia and the South Sandwich Islands
SS	South Sudan
UM	United States Minor Outlying Islands
[INFO ] OrmLearnApplication - End - testSearchContainingOrdered
[INFO ] OrmLearnApplication - Start - testAlphabetIndex
ZM	Zambia
ZW	Zimbabwe
[INFO ] OrmLearnApplication - End - testAlphabetIndex
```

This project was compiled and run against a real local MySQL 8.0 instance
while being generated, and the output above is the actual captured output.
