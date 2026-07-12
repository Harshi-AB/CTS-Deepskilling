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
 * Hands on 3 - Spring Core: Incorporate Logging.
 *
 * From this exercise onward, System.out.println() is no longer used;
 * all output goes through the SLF4J Logger, configured via
 * application.properties (logging.level.*, logging.pattern.console).
 */
@SpringBootApplication
public class SpringLearnApplication {

    // Logger instance for this class, obtained via SLF4J's LoggerFactory.
    // By convention this is a private static final field.
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
    }

    /**
     * Loads the "dateFormat" bean from date-format.xml and parses a
     * sample date String, logging progress with INFO logs at the start
     * and end of the method, and the parsed result with a DEBUG log.
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
}
