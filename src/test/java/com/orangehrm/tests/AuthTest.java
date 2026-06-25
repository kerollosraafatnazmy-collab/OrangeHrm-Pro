package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {


    private static final String authCode = "def5020019d2891addd0def9664d05ca268cbd1ff5fa6f6528e27231a3480a376b20a2079ef0b54da6f2a506e40649e318aab6ce3d3e3c587f965f8b8467360e1c95370a67ca52847c7e5e91a40cf2176201610d8ef81f3fccbf05fa625f6e6bb30d6fa9766cb1509706117bc1694489ccd1521c5427891739980d6f76679191fbaab1c7006dc485acd2c32f375b2b63b6826aa23c51bcfeab2666fcb6eb97845b7a4329e91d21d0851b09a93ca83ca4b7da66cf4db716d3ba0ee9305f514cd006af5372c498349ce97d5eec7d84f010d8a02e144e0ce040914d8d0d74ea9a14e5fece8146ef4a488908e3d09931648a6cdb9ca2a439af393af60d6f0f1c69cbb373320b1f8516be153046bd0f8895c498acc136f3d3015c399acadd790f1dfd8b20a3c85063d33095089f1c6aa02c1c5eeae6ac00c38d6e5a0bce5e00c6731fb0e53d7aa70a9d9028ef983836263f958e0f352c906434dd332bc1bd3de7e7df2ae603e719cde3c584eb37cec2f24f42592c1b650ccc1a5331e900331bbe6a3c02ace9e1b2bf5cef1805f2ff8303b8fa86d02c2a9ebc";

    private static final String codeVerifier = "6e21981fbec5c43a1801a17109cb22e79c2e8d52f5180e21936a6760";

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