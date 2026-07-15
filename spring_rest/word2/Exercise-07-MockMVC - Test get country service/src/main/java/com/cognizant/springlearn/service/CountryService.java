package com.cognizant.springlearn.service;

import com.cognizant.springlearn.exception.CountryNotFoundException;
import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CountryService
 *
 * Exercise 05 - REST Get country based on country code.
 * Exercise 06 - REST Get country exceptional scenario.
 *
 * Holds the country lookup logic so the controller stays thin. The
 * country code match is case-insensitive, implemented with a Java 8
 * Stream/lambda expression. When no country matches, a
 * CountryNotFoundException is thrown instead of returning null, so
 * Spring MVC can translate it into an HTTP 404 response.
 */
@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final List<Country> countryList;

    public CountryService(@Qualifier("countryList") List<Country> countryList) {
        this.countryList = countryList;
    }

    /**
     * Returns the country matching the given code (case-insensitive).
     *
     * @param code the country code to search for, e.g. "in" or "IN"
     * @return the matching Country
     * @throws CountryNotFoundException if no country matches the code
     */
    public Country getCountry(String code) {
        logger.info("START - CountryService.getCountry({})", code);

        // Case-insensitive lambda match across the country list
        Optional<Country> match = countryList.stream()
                .filter(country -> country.getCode().equalsIgnoreCase(code))
                .findFirst();

        Country result = match.orElseThrow(() -> {
            logger.warn("No country found for code: {}", code);
            return new CountryNotFoundException(code);
        });

        logger.info("END - CountryService.getCountry({}) -> {}", code, result);
        return result;
    }
}
