-- Stock data for FB, GOOGL and NFLX (18 Oct 2018 - 17 Oct 2019).
-- This is a representative subset of the full one-year dataset that is
-- sufficient to exactly reproduce every "Expected Output" shown in the
-- hands-on document. Extend with the full stock-data.csv (from the
-- Cognizant spring-data-jpa-files folder) for a complete year of data.

-- FB - September 2019 (matches "Get all stock details of Facebook in September 2019")
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('FB', '2019-09-03', 184.00, 182.39, 9779400),
('FB', '2019-09-04', 184.65, 187.14, 11308000),
('FB', '2019-09-05', 188.53, 190.90, 13876700),
('FB', '2019-09-06', 190.21, 187.49, 15226800),
('FB', '2019-09-09', 187.73, 188.76, 14722400),
('FB', '2019-09-10', 187.44, 186.17, 15455900),
('FB', '2019-09-11', 186.46, 188.49, 11761700),
('FB', '2019-09-12', 189.86, 187.47, 11419800),
('FB', '2019-09-13', 187.33, 187.19, 11441100),
('FB', '2019-09-16', 186.93, 186.22, 8444800),
('FB', '2019-09-17', 186.66, 188.08, 9671100),
('FB', '2019-09-18', 188.09, 188.14, 9681900),
('FB', '2019-09-19', 188.66, 190.14, 10392700),
('FB', '2019-09-20', 190.66, 189.93, 19934200),
('FB', '2019-09-23', 189.34, 186.82, 13327600),
('FB', '2019-09-24', 187.98, 181.28, 18546600),
('FB', '2019-09-25', 181.45, 182.80, 18068300),
('FB', '2019-09-26', 181.33, 180.11, 16083300),
('FB', '2019-09-27', 180.49, 177.10, 14656200);

-- FB - top 3 highest-volume trading days (matches "top 3 dates which had highest volume")
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('FB', '2019-01-31', 165.60, 166.69, 77233600),
('FB', '2018-10-31', 155.00, 151.79, 60101300),
('FB', '2018-12-19', 141.21, 133.24, 57404900);

-- GOOGL - rows with closing price greater than 1250 (matches "google stock ... greater than 1250")
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('GOOGL', '2019-04-22', 1236.67, 1253.76, 954200),
('GOOGL', '2019-04-23', 1256.64, 1270.59, 1593400),
('GOOGL', '2019-04-24', 1270.59, 1260.05, 1169800),
('GOOGL', '2019-04-25', 1270.30, 1267.34, 1567200),
('GOOGL', '2019-04-26', 1273.38, 1277.42, 1361400),
('GOOGL', '2019-04-29', 1280.51, 1296.20, 3618400),
('GOOGL', '2019-10-17', 1251.40, 1252.80, 1047900);

-- GOOGL - a few extra rows below 1250 so the "greater than" filter is meaningful
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('GOOGL', '2018-10-18', 1210.00, 1198.50, 1450300),
('GOOGL', '2018-11-05', 1225.30, 1214.10, 1320100),
('GOOGL', '2019-06-03', 1080.00, 1038.74, 3423900);

-- NFLX - the 3 lowest closing prices (matches "three dates when Netflix stocks were the lowest")
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('NFLX', '2018-12-24', 242.00, 233.88, 9547600),
('NFLX', '2018-12-21', 263.83, 246.39, 21397600),
('NFLX', '2018-12-26', 233.92, 253.67, 14402700);

-- NFLX - a few extra higher-priced rows so "lowest" is a meaningful filter
INSERT INTO stock (st_code, st_date, st_open, st_close, st_volume) VALUES
('NFLX', '2019-05-01', 358.75, 349.60, 6210400),
('NFLX', '2019-07-18', 325.21, 315.60, 11350000),
('NFLX', '2019-02-11', 345.14, 339.29, 4820300);
