package com.cognizant.springlearn;

import com.cognizant.springlearn.controller.CountryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SpringLearnApplicationTests
 *
 * Exercise 07 - MockMVC: Test get country service.
 *
 * @AutoConfigureMockMvc gives us a MockMvc instance that exercises the
 * full Spring MVC request-handling pipeline (controller, JSON
 * converters, etc.) WITHOUT starting a real HTTP server on a port.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringLearnApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Sanity check that the Spring application context starts and the
     * CountryController bean was created successfully.
     */
    @Test
    public void contextLoads() {
        assertNotNull(countryController);
    }

    /**
     * Exercise 07 - verifies GET /country returns HTTP 200 with the
     * expected India JSON payload.
     */
    @Test
    public void testGetCountry() throws Exception {
        mockMvc.perform(get("/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IN"))
                .andExpect(jsonPath("$.name").value("India"));
    }

    /**
     * Exercise 08 - MockMVC: Test get country service for exceptional
     * scenario.
     *
     * Requests a country code that does not exist and verifies that
     * CountryNotFoundException is translated into an HTTP 404 response
     * with reason "Country not found", matching the @ResponseStatus
     * declared on CountryNotFoundException in Exercise 06.
     *
     * Note: the original hands-on document's test snippet checks
     * status().isBadRequest() (HTTP 400), but the exception class it
     * asks you to build in Exercise 06 is annotated
     * @ResponseStatus(HttpStatus.NOT_FOUND) (HTTP 404). This test
     * checks isNotFound() so it is consistent with the actual
     * implementation from Exercise 06 rather than the mismatched
     * status code in the test snippet.
     */
    @Test
    public void testGetCountryException() throws Exception {
        mockMvc.perform(get("/countries/xx"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Country not found"));
    }
}
