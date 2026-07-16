import java.util.regex.Pattern;

/**
 * CountryValidator.java
 *
 * Encapsulates validation rules for Country data, in particular the
 * country code, which must follow the ISO 3166-1 alpha-2 style:
 * exactly two uppercase letters (e.g. "IN", "US", "FR").
 *
 * Keeping validation logic in its own class (rather than scattered inside
 * the handler) makes the rules reusable and independently testable.
 */
public class CountryValidator {

    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");

    /**
     * Validates a country code.
     *
     * @param code the code to validate
     * @throws IllegalArgumentException if the code is null, blank, or does
     *         not match the required two-uppercase-letter format
     */
    public void validateCountryCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Country code is required.");
        }
        if (!COUNTRY_CODE_PATTERN.matcher(code).matches()) {
            throw new IllegalArgumentException(
                    "Invalid country code '" + code + "'. Expected exactly two uppercase letters, e.g. 'IN'.");
        }
    }

    /** Validates the full Country bean; delegates code validation and checks the name. */
    public void validate(Country country) {
        validateCountryCode(country.getCode());
        if (country.getName() == null || country.getName().isBlank()) {
            throw new IllegalArgumentException("Country name is required.");
        }
    }
}
