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
 * Hands on 4 - Spring Core: Load Country from Spring Configuration XML.
 *
 * Adds the displayCountry() method, which loads the "country" bean
 * (a com.cognizant.springlearn.Country instance) from country.xml and
 * logs its details.
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
        app.displayCountry();
    }

    /**
     * Loads the "dateFormat" bean from date-format.xml and parses a
     * sample date String (Hands on 2 & 3).
     */
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
     * Loads the "country" bean from country.xml (Spring IoC container)
     * and logs the details of the retrieved Country object.
     */
    public void displayCountry() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("country", Country.class);

        LOGGER.debug("Country : {}", country.toString());

        ((ClassPathXmlApplicationContext) context).close();

        LOGGER.info("END");
    }
}
