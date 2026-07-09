import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CountryDataLoader
 * -----------------
 * Loads seed data from sql/countries.sql (plain SQL insert statements) into
 * the in-memory MySQLDatabase, via CountryRepository.save(). This class
 * fills the role that actually executing countries.sql against a real
 * MySQL 8.0 instance would play - reading the same DDL/DML you'd run in
 * MySQL Workbench, but using only core java.io + java.util.regex (no JDBC
 * driver jar), matching this project's "no external jars" constraint.
 */
public class CountryDataLoader {

    private static final Pattern INSERT_PATTERN = Pattern.compile(
            "insert into country \\(co_code, co_name\\) values \\('([^']+)',\\s*'((?:[^']|'')*)'\\);",
            Pattern.CASE_INSENSITIVE);

    /**
     * Reads every "insert into country (co_code, co_name) values (...)"
     * statement from the given .sql file and saves each row through the
     * repository (exercising the exact same save() path production code uses).
     *
     * @return number of country rows loaded
     */
    public static int loadFromSqlFile(String sqlFilePath, CountryRepository countryRepository) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sqlFilePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = INSERT_PATTERN.matcher(line.trim());
                if (matcher.matches()) {
                    String code = matcher.group(1);
                    String name = matcher.group(2).replace("''", "'");
                    countryRepository.save(new Country(code, name));
                    count++;
                }
            }
        }
        return count;
    }
}
