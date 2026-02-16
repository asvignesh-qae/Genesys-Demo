package com.vignesh.stepdefinitions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vignesh.config.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class RestAssuredApiTestSteps {

    private static final Logger logger = LogManager.getLogger(RestAssuredApiTestSteps.class);

    private Response response;
    private JsonArray users;

    @Given("the API base URL is configured")
    public void theApiBaseUrlIsConfigured() {
        RestAssured.baseURI = ConfigReader.getApiBaseUrl();
        logger.info("API base URL configured: {}", RestAssured.baseURI);
    }

    @When("user sends a GET request to {string} endpoint")
    public void userSendsAGetRequestToEndpoint(String endpoint) {
        logger.info("Sending GET request to {}", endpoint);
        response = RestAssured
                .given()
                .filter(new AllureRestAssured())
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        logger.info("Response received with status code: {}", response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        logger.info("Verifying response status code is {}", expectedStatusCode);
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Expected status code " + expectedStatusCode);
        logger.info("Status code validation passed: {}", response.getStatusCode());
    }

    @And("the response should contain a non-empty list of users")
    public void theResponseShouldContainANonEmptyListOfUsers() {
        logger.info("Parsing response to JSON and verifying users list is not empty");
        users = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();
        logger.info("Total users found: {}", users.size());
        Assert.assertTrue(users.size() > 0, "Users list should not be empty");
        logger.info("Users list validation passed");
    }

    @And("user logs the name and email of each user")
    public void userLogsTheNameAndEmailOfEachUser() {
        logger.info("Logging name and email of each user:");
        for (JsonElement element : users) {
            String name = element.getAsJsonObject().get("name").getAsString();
            String email = element.getAsJsonObject().get("email").getAsString();
            logger.info("{} | {}", name, email);
        }
        logger.info("All user names and emails logged");
    }

    @And("the first user email should contain {string}")
    public void theFirstUserEmailShouldContain(String expectedSubstring) {
        String firstEmail = users.get(0).getAsJsonObject().get("email").getAsString();
        logger.info("Verifying first user email '{}' contains '{}'", firstEmail, expectedSubstring);
        Assert.assertTrue(firstEmail.contains(expectedSubstring),
                "First email should contain '" + expectedSubstring + "'. Actual: " + firstEmail);
        logger.info("First user email validation passed: '{}'", firstEmail);
    }
}
