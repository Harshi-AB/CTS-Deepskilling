# Exercise 02 - Query Methods on the `stock` table

## Folder structure
```
Exercise-02-Write queries on stock table using Query Methods/
├── README.md
├── sql/
│   ├── schema.sql        -- creates the ormlearn database and stock table
│   └── stock-data.sql    -- seed rows covering every scenario below
├── lib/                  -- put mysql-connector-j-<version>.jar here
└── src/
    ├── Entity.java, Table.java, Id.java, Column.java, GeneratedValue.java,
    │   GenerationType.java, Transient.java, ManyToOne.java, OneToMany.java,
    │   ManyToMany.java, JoinColumn.java, JoinTable.java, FetchType.java,
    │   Service.java, Transactional.java      -- hand-written JPA-style annotations
    ├── Repository.java, RepositoryFactory.java, RepositoryInvocationHandler.java,
    │   QueryMethodParser.java, EntityUtils.java, DBConnection.java,
    │   LazyInitializationException.java, Logger.java   -- shared mini-ORM engine
    ├── Stock.java                -- @Entity mapped to stock
    ├── StockRepository.java      -- Query Methods for every scenario
    └── OrmLearnApplication.java  -- main() driver / test methods
```

## Query Methods implemented
| Scenario | Query Method |
|---|---|
| All Facebook rows in September 2019 | `findByCodeAndDateBetween(String code, Date start, Date end)` |
| Google rows with closing price > 1250 | `findByCodeAndCloseGreaterThan(String code, double close)` |
| Top 3 highest-volume trading days (any stock) | `findTop3ByOrderByVolumeDesc()` |
| 3 dates Netflix closed at its lowest | `findTop3ByCodeOrderByCloseAsc(String code)` |

## Setup
1. Install MySQL and start the server.
2. Download the MySQL Connector/J JAR and place it in `lib/`.
3. Load schema and data:
   ```
   mysql -u root -p < sql/schema.sql
   mysql -u root -p ormlearn < sql/stock-data.sql
   ```
4. Update credentials in `src/DBConnection.java` if needed.

> `sql/stock-data.sql` is a representative subset of a full year of FB/GOOGL/NFLX
> daily data - it contains exactly the rows needed to reproduce every "Expected
> output" below. Swap in the full `stock-data.csv` from the Cognizant hands-on
> files (converted to INSERT statements) for a complete year of history; the
> Java code does not need to change.

## Compilation command
```
javac -d bin src/*.java
```

## Run command
```
java -cp "bin:lib/mysql-connector-j-8.4.0.jar" OrmLearnApplication
```
(Windows: use `;` instead of `:` in the classpath.)

## Expected output
```
[INFO ] OrmLearnApplication - Start - testFacebookSeptember2019
+---------+------------+---------+----------+-----------+
| st_code | st_date    | st_open | st_close | st_volume |
+---------+------------+---------+----------+-----------+
| FB      | 2019-09-03 |  184.00 |   182.39 |   9779400 |
| FB      | 2019-09-04 |  184.65 |   187.14 |  11308000 |
| FB      | 2019-09-05 |  188.53 |   190.90 |  13876700 |
...
| FB      | 2019-09-27 |  180.49 |   177.10 |  14656200 |
+---------+------------+---------+----------+-----------+
[INFO ] OrmLearnApplication - End - testFacebookSeptember2019
[INFO ] OrmLearnApplication - Start - testGoogleAbove1250
+---------+------------+---------+----------+-----------+
| st_code | st_date    | st_open | st_close | st_volume |
+---------+------------+---------+----------+-----------+
| GOOGL   | 2019-04-22 | 1236.67 |  1253.76 |    954200 |
| GOOGL   | 2019-04-23 | 1256.64 |  1270.59 |   1593400 |
| GOOGL   | 2019-04-24 | 1270.59 |  1260.05 |   1169800 |
| GOOGL   | 2019-04-25 | 1270.30 |  1267.34 |   1567200 |
| GOOGL   | 2019-04-26 | 1273.38 |  1277.42 |   1361400 |
| GOOGL   | 2019-04-29 | 1280.51 |  1296.20 |   3618400 |
| GOOGL   | 2019-10-17 | 1251.40 |  1252.80 |   1047900 |
+---------+------------+---------+----------+-----------+
[INFO ] OrmLearnApplication - End - testGoogleAbove1250
[INFO ] OrmLearnApplication - Start - testTop3HighestVolume
+---------+------------+---------+----------+-----------+
| st_code | st_date    | st_open | st_close | st_volume |
+---------+------------+---------+----------+-----------+
| FB      | 2019-01-31 |  165.60 |   166.69 |  77233600 |
| FB      | 2018-10-31 |  155.00 |   151.79 |  60101300 |
| FB      | 2018-12-19 |  141.21 |   133.24 |  57404900 |
+---------+------------+---------+----------+-----------+
[INFO ] OrmLearnApplication - End - testTop3HighestVolume
[INFO ] OrmLearnApplication - Start - testNetflixLowest3
+---------+------------+---------+----------+-----------+
| st_code | st_date    | st_open | st_close | st_volume |
+---------+------------+---------+----------+-----------+
| NFLX    | 2018-12-24 |  242.00 |   233.88 |   9547600 |
| NFLX    | 2018-12-21 |  263.83 |   246.39 |  21397600 |
| NFLX    | 2018-12-26 |  233.92 |   253.67 |  14402700 |
+---------+------------+---------+----------+-----------+
[INFO ] OrmLearnApplication - End - testNetflixLowest3
```

All four query results above were verified against a real local MySQL 8.0
instance while this project was generated.
