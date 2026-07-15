package com.cognizant.springlearn.service;

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
 *
 * Holds the country lookup logic so the controller stays thin. The
 * country code match is case-insensitive, implemented with a Java 8
 * Stream/lambda expression instead of a manual for-loop.
 */
@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final List<Country> countryList;

    public CountryService(@Qualifier("countryList") List<Country> countryList) {
        this.countryList = countryList;
    }

    /**
     * Returns the country matching the given code (case-insensitive),
     * or null if no match exists.
     *
     * @param code the country code to search for, e.g. "in" or "IN"
     * @return the matching Country, or null when not found
     */
    public Country getCountry(String code) {
        logger.info("START - CountryService.getCountry({})", code);

        // Case-insensitive lambda match across the country list
        Optional<Country> match = countryList.stream()
                .filter(country -> country.getCode().equalsIgnoreCase(code))
                .findFirst();

        Country result = match.orElse(null);

        logger.info("END - CountryService.getCountry({}) -> {}", code, result);
        return result;
    }
}
