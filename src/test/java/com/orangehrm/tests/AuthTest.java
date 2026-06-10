package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    private static String authCode = "def50200b92cb8ad0ea468ef49da9e1e4d2b59c12ba128d6d4964477a45512d16618131dc1a30d282de1c969ef521777da2c396f3fc088d875c60166a64c6b8956784f63088a3b4e7a44055016f95b9073adc776f43ded3d9074f44152ffdb95bacfd3c5af8e5d54ee5b310a5bf6a8a7bbcdb83e0cbefc7b3c95c3cc30d9e3afa2881bec3915ee2b24d2381b5fce8a760290303ac2f34b564021ec34abf5ddbfd4b64ad29f2b4c509579693a6dbf41f11944748d48a41cefa4261b1a9822351b2c6be3d70ce2af57ac6d1959cddac011dd20c86435fca8b777f34202c292d9d47aee92600671c333aac02ddbf38d3097024e74f4d071d3a10723b9a4f7b97a77350fc4667d47412287ef6e4b0482395cdd2423b3d2da4863c1ed90df61cd9fcfa024169c2df31589334731d37c5ae9f3f453b267d4026f28ad4a81836aa07973057e7698f9ed603bbf441077faa0945b406e45d2db8963d1d8cb74376f7678a86e61d278bcf329e17c56397ea5aa87a1d39af460659b98c9ebe7269356ee77a97b07e2e9cefdffa6d8057d94e505f05c095347f91cf7";

    private static String codeVerifier = "f439add3474beb0c415d19e7ba2ef4f1f5ca127b7bbad771799c5e1c";

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