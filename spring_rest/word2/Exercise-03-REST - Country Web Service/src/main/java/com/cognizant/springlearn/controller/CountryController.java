package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CountryController
 *
 * Exercise 03 - REST Country Web Service.
 *
 * Exposes GET /country, returning the India Country bean that is
 * defined in country-beans.xml and loaded into the Spring application
 * context via @ImportResource on SpringLearnApplication.
 *
 * Sample Request:  http://localhost:8083/country
 * Sample Response: { "code": "IN", "name": "India" }
 */
@RestController
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private final Country indiaCountry;

    // Constructor injection of the "indiaCountry" bean defined in country-beans.xml
    public CountryController(@Qualifier("indiaCountry") Country indiaCountry) {
        logger.info("CountryController constructed - bean injected: {}", indiaCountry);
        this.indiaCountry = indiaCountry;
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
}
