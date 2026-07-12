package com.cognizant.springlearn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringLearnApplication
 *
 * Hands on 5 - Spring Core: Demonstration of Singleton Scope and
 * Prototype Scope.
 *
 * displayCountry() now retrieves the "country" bean TWICE from the
 * same ApplicationContext (country.xml, default "singleton" scope).
 * Because the bean is a singleton, the Country constructor only runs
 * once, and both references point to the exact same object - watch the
 * log output: "Inside Country Constructor." appears only once, and
 * "country == anotherCountry" prints true.
 *
 * displayCountryPrototypeScope() does the same thing but loads
 * country-prototype.xml instead, where the bean is declared with
 * scope="prototype". Here the constructor runs twice (once per
 * getBean() call) and "country == anotherCountry" prints false.
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
        app.displayCountry();
        app.displayCountryPrototypeScope();
    }

    /** Hands on 2 & 3: load and parse date using dateFormat bean. */
    public void displayDate() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);

        try {
            Date date = format.parse("31/12/2018");
            LOGGER.debug("Parsed Date : {}", date);
        } catch (ParseException e) {
            LOGGER.debug("Unable to parse date : {}", e.getMessage());
        } finally {
            ((ClassPathXmlApplicationContext) context).close();
        }

        LOGGER.info("END");
    }

    /**
     * Demonstrates SINGLETON scope (the default scope in Spring).
     * country.xml does not specify a "scope" attribute on the "country"
     * bean, so Spring uses the default "singleton" scope - only ONE
     * instance is ever created per ApplicationContext, no matter how
     * many times getBean() is called.
     */
    public void displayCountry() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");

        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);

        LOGGER.debug("Country : {}", country.toString());
        LOGGER.debug("anotherCountry : {}", anotherCountry.toString());
        LOGGER.debug("country == anotherCountry (singleton scope) : {}", (country == anotherCountry));

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END");
    }

    /**
     * Demonstrates PROTOTYPE scope. country-prototype.xml declares the
     * "country" bean with scope="prototype", so Spring creates a NEW
     * instance of Country every time getBean() is invoked.
     */
    public void displayCountryPrototypeScope() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country-prototype.xml");

        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);

        LOGGER.debug("Country : {}", country.toString());
        LOGGER.debug("anotherCountry : {}", anotherCountry.toString());
        LOGGER.debug("country == anotherCountry (prototype scope) : {}", (country == anotherCountry));

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END");
    }
}
