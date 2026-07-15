package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CountryController
 *
 * Exercises 03-06 - REST Country Web Service and follow-ons.
 *
 * Exposes GET /country, returning the India Country bean that is
 * defined in country.xml and loaded into the Spring application
 * context via @ImportResource on SpringLearnApplication.
 *
 * Sample Request:  http://localhost:8083/country
 * Sample Response: { "code": "IN", "name": "India" }
 */
@RestController
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private final Country indiaCountry;
    private final List<Country> countryList;
    private final CountryService countryService;

    // Constructor injection of the "indiaCountry" / "countryList" beans
    // defined in country.xml, plus the CountryService used from Exercise 05.
    public CountryController(@Qualifier("indiaCountry") Country indiaCountry,
                              @Qualifier("countryList") List<Country> countryList,
                              CountryService countryService) {
        logger.info("CountryController constructed - bean injected: {}", indiaCountry);
        this.indiaCountry = indiaCountry;
        this.countryList = countryList;
        this.countryService = countryService;
    }

    @RequestMapping("/country")
    public Country getCountryIndia() {
        logger.info("START - CountryController.getCountryIndia()");

        // The Country bean is simply returned; Spring's Jackson message
        // converter serializes its getters into the JSON response body.
        Country result = indiaCountry;

        logger.info("END - CountryController.getCountryIndia()");
        return result;
    }

    /**
     * Exercise 04 - REST Get all countries.
     *
     * Sample Request:  http://localhost:8083/countries
     * Sample Response: [ { "code": "IN", "name": "India" }, ... ]
     */
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        logger.info("START - CountryController.getAllCountries()");

        List<Country> result = countryList;

        logger.info("END - CountryController.getAllCountries()");
        return result;
    }

    /**
     * Exercise 05 - REST Get country based on country code.
     * Exercise 06 - REST Get country exceptional scenario.
     *
     * The @PathVariable code is matched case-insensitively by
     * CountryService.getCountry(). If no country matches, the service
     * throws CountryNotFoundException, which Spring MVC automatically
     * converts into an HTTP 404 response (see @ResponseStatus on
     * CountryNotFoundException) - no explicit try/catch needed here.
     *
     * Sample Request (found):     http://localhost:8083/countries/in
     * Sample Response (found):    { "code": "IN", "name": "India" }
     *
     * Sample Request (not found): http://localhost:8083/countries/xx
     * Sample Response (not found): HTTP 404, reason "Country not found"
     */
    @GetMapping("/countries/{code}")
    public Country getCountry(@PathVariable String code) {
        logger.info("START - CountryController.getCountry({})", code);

        Country result = countryService.getCountry(code);

        logger.info("END - CountryController.getCountry({}) -> {}", code, result);
        return result;
    }
}
