package com.cognizant.springlearn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CountryNotFoundException
 *
 * Exercise 06 - REST Get country exceptional scenario.
 *
 * Thrown by CountryService when no country matches the requested code.
 * @ResponseStatus tells Spring MVC to automatically translate this
 * exception into an HTTP 404 (Not Found) response, with the given
 * reason phrase, without needing an explicit @ExceptionHandler.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
public class CountryNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(String code) {
        super("Country not found for code: " + code);
    }
}
