package org.specs;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestInstance(Lifecycle.PER_CLASS)
public class CustomerCreateTest {

    private final static String HOST = "localhost";
    private final static String PORT = "8080";
    private final static String BASE_URI = String.format("http://%s:%s", HOST, PORT);
    private final static String ACCESS_TOKEN_ENDPOINT = "/oauth2/access_token";

    private WireMockServer wireMockServer;
    private String authToken;

    @BeforeAll
    public void setUp() {
        
        wireMockServer = new WireMockServer(Integer.parseInt(PORT));
        wireMockServer.start();
        configureFor(HOST, Integer.parseInt(PORT));
    }

    @AfterAll
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void verifyAccessTokenIsGranted() {
        
        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("grant_type", "password");
        jsonBody.put("username", "testuser");
        jsonBody.put("password", "password123");


        Response response = RestAssured.given()
            .baseUri(BASE_URI)
            .header("Content-Type", "application/json")
            .body(jsonBody)
            .post(ACCESS_TOKEN_ENDPOINT);

        assertEquals(200, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().jsonPath().getString("message"));
        authToken = response.getBody().jsonPath().getString("authToken");
        assertNotNull(authToken);
    }

    @Test
    public void verifyWrongPasswordFailure() {
        
        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("grant_type", "password");
        jsonBody.put("username", "testuser");
        jsonBody.put("password", "wrong_password");


        Response response = RestAssured.given()
            .baseUri(BASE_URI)
            .header("Content-Type", "application/json")
            .body(jsonBody)
            .post(ACCESS_TOKEN_ENDPOINT);

        assertTrue(response.getStatusCode() >= 400);
    }
    
}
