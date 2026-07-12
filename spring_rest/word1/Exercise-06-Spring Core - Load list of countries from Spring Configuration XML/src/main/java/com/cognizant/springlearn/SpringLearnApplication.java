package com.cognizant.springlearn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringLearnApplication
 *
 * Hands on 6 - Spring Core: Load list of countries from Spring
 * Configuration XML.
 *
 * Adds the displayCountries() method, which loads the "countryList"
 * bean (a java.util.ArrayList&lt;Country&gt; built from four individual
 * Country beans via &lt;list&gt;/&lt;ref&gt; in country-list.xml) and logs
 * each country in the list - fulfilling the original requirement of
 * retrieving all four countries supported by the airlines website.
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
        app.displayCountries();
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

    /** Hands on 4 & 5: singleton scope demonstration with a single country. */
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

    /** Hands on 5: prototype scope demonstration with a single country. */
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

    /**
     * Loads the "countryList" bean from country-list.xml and logs each
     * of the four Country objects it contains.
     */
    @SuppressWarnings("unchecked")
    public void displayCountries() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country-list.xml");

        List<Country> countryList = (List<Country>) context.getBean("countryList", List.class);

        for (Country country : countryList) {
            LOGGER.debug("Country : {}", country.toString());
        }

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END");
    }
}
