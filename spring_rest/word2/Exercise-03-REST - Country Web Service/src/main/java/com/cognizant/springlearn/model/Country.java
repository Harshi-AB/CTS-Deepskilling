package com.cognizant.springlearn.model;

/**
 * Country
 *
 * Simple POJO representing a country. Instances of this bean are
 * defined in the Spring XML configuration (country-beans.xml) and
 * are converted to JSON automatically by Spring's Jackson integration
 * when returned from a @RestController method.
 */
public class Country {

    private String code;
    private String name;

    public Country() {
        // Default constructor required for Spring XML bean instantiation
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
