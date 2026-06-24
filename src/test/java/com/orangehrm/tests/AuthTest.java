package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {


    private static final String authCode = "def50200ab0c6ca4cd0de85da2115679fa0e80033b61088746fd200bdc87a5fb49b1884ccf6ecc9df2d5abcee18b6817401ac434cf7c8d15eb1ab52ea0bdea239c19aa15308761ec2e8454257ac6e3cf5517c0d864fac4afd01f09e7165a1f8c068cf1d86284e218f94a3bb18ed957df22d4233442085a4ad86aea275668284a199885ab329d5055e0529595e784166a43b18bc20d32c76a6fa29fe6a0fb05746b98e3d2b5ba3beec3ceff2301898db906bb6afb82a9e3356e8dc9dd9a6e338e1fbf4022012de2f48bebbba1a7232e9c7e4717be58e451b77c574cfe6e1be785ebaa731a9f6b0c1505b5723ef129b5da95cd74959fd05cc219977ee1b7dc0541fbd746541adbcf433b8abebd98ca71849e24c5395207690b01618af11f3e00035c3595ee47c71ac07197729e65e151e3131caa130b58d5de9722de0c41b8835ac456ed5ddfe51e294bd5ccc74b9fbff8f09af69563dbf722344d20336df85586cc45cf98291c6058b6230d09000bfc2e09415d2a4f2a691d80b0f5c4d7c78c7b9f01fad4f3b2d93997190c740e4e6e669d20275934fe";

    private static final String codeVerifier = "5bd2d5741e50dfdb137af4885946f44ed74c407b3567b581e798a028";

    @Test(priority = 1, description = "Obtain Access Token via PKCE")
    public void testObtainAccessToken() {

        Response response = RestAssured
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "authorization_code")
                .formParam("code", authCode)
                .formParam("client_id", ConfigManager.getClientId())
                .formParam("redirect_uri", ConfigManager.getRedirectUri())
                .formParam("code_verifier", codeVerifier)
                .when()
                .post("/oauth2/token")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        TokenManager.setTokensFromResponse(response);
        System.out.println("✅ Access Token: " + response.jsonPath().getString("access_token"));
        System.out.println("📋 REFRESH TOKEN: " + response.jsonPath().getString("refresh_token"));

        Assert.assertNotNull(response.jsonPath().getString("access_token"));
        Assert.assertNotNull(response.jsonPath().getString("refresh_token"));
    }

    @Test(priority = 2, description = "Refresh Access Token",
            dependsOnMethods = "testObtainAccessToken")
    public void testRefreshToken() {
        TokenManager.refreshAccessToken();
        Assert.assertNotNull(TokenManager.getAccessToken());
        System.out.println("✅ Token Refreshed!");
    }
}