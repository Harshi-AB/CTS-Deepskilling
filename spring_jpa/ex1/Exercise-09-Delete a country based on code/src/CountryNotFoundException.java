/**
 * CountryNotFoundException
 * ------------------------
 * Thrown by CountryService.findCountryByCode() when no country row exists
 * for the given code. Conceptually lives in
 * com.cognizant.spring-learn.service.exception per the exercise document;
 * kept as a top-level class here since this project has no package
 * declarations.
 */
public class CountryNotFoundException extends Exception {

    public CountryNotFoundException(String message) {
        super(message);
    }
}
