package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Country
 *
 * Simple POJO (Plain Old Java Object) representing a country supported
 * by the airlines website's country drop-down. Instances of this class
 * are configured and instantiated by the Spring IoC container from
 * country.xml, using the no-argument constructor followed by setter
 * (property) injection.
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    /** Two-character ISO code of the country, e.g. "IN", "US". */
    private String code;

    /** Full display name of the country, e.g. "India", "United States". */
    private String name;

    /**
     * No-argument constructor required by Spring for setter-based
     * dependency injection.
     */
    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    public String getCode() {
        LOGGER.debug("Inside getCode() method.");
        return code;
    }

    public void setCode(String code) {
        LOGGER.debug("Inside setCode() method.");
        this.code = code;
    }

    public String getName() {
        LOGGER.debug("Inside getName() method.");
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("Inside setName() method.");
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [code=" + code + ", name=" + name + "]";
    }
}
