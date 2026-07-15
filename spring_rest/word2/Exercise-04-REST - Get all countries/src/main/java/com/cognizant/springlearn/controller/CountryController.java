package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
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

    // Constructor injection of the "indiaCountry" and "countryList" beans
    // defined in country.xml
    public CountryController(@Qualifier("indiaCountry") Country indiaCountry,
                              @Qualifier("countryList") List<Country> countryList) {
        logger.info("CountryController constructed - bean injected: {}", indiaCountry);
        this.indiaCountry = indiaCountry;
        this.countryList = countryList;
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
}
