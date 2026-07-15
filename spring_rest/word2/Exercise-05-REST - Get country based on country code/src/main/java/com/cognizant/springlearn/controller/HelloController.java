package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * Exercise 02 - Hello World RESTful Web Service.
 *
 * Exposes a single GET endpoint at /hello that returns the plain text
 * "Hello World!!". @RestController combines @Controller and
 * @ResponseBody, so the String returned below is written directly to
 * the HTTP response body instead of being resolved as a view name.
 *
 * Sample Request:  http://localhost:8083/hello
 * Sample Response: Hello World!!
 */
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String sayHello() {
        logger.info("START - HelloController.sayHello()");

        String greeting = "Hello World!!";

        logger.info("END - HelloController.sayHello()");
        return greeting;
    }
}
