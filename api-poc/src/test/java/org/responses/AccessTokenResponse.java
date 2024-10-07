package org.responses;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import io.restassured.path.json.JsonPath;

public class AccessTokenResponse implements ResponseDefinitionTransformerV2  {

    private static final String ACCEPT_MEDIA_TYPE = "application/json";
    private static final String CORRECT_USER = "user123";
    private static final String CORRECT_PASSWORD = "password123";
    private static final Integer CORRECT_STATUS_CODE = 200;
    private static final Integer INVALID_REQUEST_STATUS_CODE = 400;
    private static final Integer INVALID_CREDENTIALS_STATUS_CODE = 401;
    private static final Integer UNSUPPORTED_MEDIA_TYPE_STATUS_CODE = 415;

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        return response.apply(serveEvent);
    }

    @Override
    public String getName() {
        return "access-token-stub-response";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    private Function<Map<String, String>, String> convertMapToJson = mapToConvert -> {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(mapToConvert);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return "";
    };

    private Function<ServeEvent, ResponseDefinition> response = serveEvent -> {

        if (!serveEvent.getRequest().contentTypeHeader().containsValue(ACCEPT_MEDIA_TYPE)) {
            return new ResponseDefinitionBuilder().withStatus(UNSUPPORTED_MEDIA_TYPE_STATUS_CODE).build();
        }

        if (serveEvent.getRequest().getBodyAsString().isEmpty()) {
            return new ResponseDefinitionBuilder().withStatus(INVALID_REQUEST_STATUS_CODE).build();
        }

        JsonPath requestBody = new JsonPath(serveEvent.getRequest().getBodyAsString());
        String userName = requestBody.getString("username");
        String userPassword = requestBody.getString("password");

        if (!userName.equals(CORRECT_USER) || !userPassword.equals(CORRECT_PASSWORD)) {
            return new ResponseDefinitionBuilder().withStatus(INVALID_CREDENTIALS_STATUS_CODE).build();
        }
       
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User registered successfully");
        responseBody.put("authToken", "abc123def");

        return new ResponseDefinitionBuilder()
                    .withHeader("Content-Type", ACCEPT_MEDIA_TYPE)
                    .withStatus(CORRECT_STATUS_CODE)
                    .withBody(convertMapToJson.apply(responseBody))
                    .build();
    };
}
