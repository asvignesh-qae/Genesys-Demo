package com.vignesh.tests;

import com.vignesh.config.ConfigReader;
import com.vignesh.listeners.TestListener;
import com.vignesh.models.User;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Listeners(TestListener.class)
public class RestApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.getApiBaseUrl();
        RestAssured.filters(new AllureRestAssured());
    }

    @Test(groups = {"regression", "testCase5"}, retryAnalyzer = RetryAnalyzer.class)
    public void testGetUsersApi() {
        ReportLogger.step("Step 1: Send GET request to /users");
        Response response = RestAssured
                .given()
                .when()
                .get("/users")
                .then()
                .extract()
                .response();

        ReportLogger.step("Step 2: Verify status code is 200");
        ReportLogger.step("Response status code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        ReportLogger.step("Step 3: Deserialize response into User POJOs");
        List<User> users = Arrays.asList(response.getBody().as(User[].class));
        ReportLogger.step("Total users found: " + users.size());
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");

        ReportLogger.step("Step 4: Validate each user has required fields");
        for (User user : users) {
            Assert.assertTrue(user.getId() > 0, "User ID should be positive");
            Assert.assertNotNull(user.getName(), "User name should not be null");
            Assert.assertNotNull(user.getUsername(), "Username should not be null");
            Assert.assertNotNull(user.getEmail(), "Email should not be null");
            Assert.assertNotNull(user.getAddress(), "Address should not be null");
            Assert.assertNotNull(user.getCompany(), "Company should not be null");
            ReportLogger.step(user.getName() + " | " + user.getEmail());
        }

        ReportLogger.step("Step 5: Validate first user's nested objects");
        User firstUser = users.get(0);
        Assert.assertTrue(firstUser.getEmail().contains("@"),
                "First email should contain '@'. Actual: " + firstUser.getEmail());
        Assert.assertNotNull(firstUser.getAddress().getCity(), "City should not be null");
        Assert.assertNotNull(firstUser.getAddress().getGeo(), "Geo should not be null");
        Assert.assertNotNull(firstUser.getAddress().getGeo().getLat(), "Latitude should not be null");
        Assert.assertNotNull(firstUser.getCompany().getName(), "Company name should not be null");

        ReportLogger.pass("GET /users API test passed");
    }
}
