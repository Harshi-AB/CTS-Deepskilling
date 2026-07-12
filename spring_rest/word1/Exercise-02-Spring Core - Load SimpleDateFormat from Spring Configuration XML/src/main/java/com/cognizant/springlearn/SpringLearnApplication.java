package com.cognizant.springlearn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringLearnApplication
 *
 * Hands on 2 - Spring Core: Load SimpleDateFormat from Spring
 * Configuration XML.
 *
 * Adds the displayDate() method which loads the "dateFormat" bean
 * (a java.text.SimpleDateFormat configured with pattern dd/MM/yyyy)
 * from date-format.xml using a ClassPathXmlApplicationContext, and
 * uses it to parse a sample date String.
 */
@SpringBootApplication
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
    }

    /**
     * Loads the Spring XML configuration file "date-format.xml", retrieves
     * the "dateFormat" bean (a pre-configured SimpleDateFormat), uses it to
     * parse the String "31/12/2018" into a java.util.Date, and prints the
     * result.
     */
    public void displayDate() {
        // Create the Spring IoC container from the XML configuration file
        // located on the classpath (src/main/resources/date-format.xml).
        ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");

        // Retrieve the "dateFormat" bean, casting it to SimpleDateFormat.
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);

        try {
            Date date = format.parse("31/12/2018");
            System.out.println("Parsed Date: " + date);
        } catch (ParseException e) {
            System.out.println("Unable to parse date: " + e.getMessage());
        } finally {
            ((ClassPathXmlApplicationContext) context).close();
        }
    }
}
