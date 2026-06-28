package com.orangehrm.utils;

import com.orangehrm.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TokenManager {

    private static String accessToken;
    private static String refreshToken;

    public static String getAccessToken() {
        if (accessToken == null) {
            refreshAccessToken();
        }
        return accessToken;
    }

    public static void setTokensFromResponse(Response response) {
        accessToken = response.jsonPath().getString("access_token");
        refreshToken = response.jsonPath().getString("refresh_token");
    }

    public static void refreshAccessToken() {
        if (refreshToken == null) {
            throw new RuntimeException("No refresh token! Run AuthTest first.");
        }

        Response response = RestAssured
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "refresh_token")
                .formParam("refresh_token", refreshToken)
                .formParam("client_id", ConfigManager.getClientId())
                .when()
                .post("/oauth2/token")
                .then()
                .statusCode(200)
                .extract().response();

        accessToken = response.jsonPath().getString("access_token");
        refreshToken = response.jsonPath().getString("refresh_token");
        System.out.println("git Token Refreshed Successfully!");
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static void setRefreshToken(String token) {
        refreshToken = token;
    }
}