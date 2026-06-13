package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {



    private static final String authCode = "def50200172c60a96e3b82664527c4ad22f2a27b7739930bdde0bcaaa8798641dcd6b0bc23e6105dd7abfd7cd336f1db2c01c942491466a978d5e2f7c6c71901dbdfd31bc3dd1a05bab5d79a823691556249f0fc08e626c45a766de37eb08fef39fb0c4edaf35550afde3b576106fd1271d117408afd6b7ddfca3029e11e9465cb3399c86064cf4bbd090b8f2d00d84cedaa9179cea449abb1cfc09d938dc3d62cb7760dc685546c35b79137f31fc73bf4801147715d12b1ae6e342f23d4f89f3b31dcc890a2986a4303f6d5edadc068ae224b18ea6d950471f9ae1a580d9761f574efabac2578a433bcf8a79f8cbda04c73e910c3a8ad58f89c720d874c2cbb27b59b8468298cf7cf388cff125440f8ecd709450d74dbed5e5b99626f670d3ea0003bf9073564f0bb8079a7844978cfc7d14eb029ce866922862e924f08594a01ac7c63d6b2ec3c94ce9071b52c7c6e3c9ae4f4bff730e61667340974e02a82a402c29797811b537f564cc8ff362ec1fea69b66b2816651a39a21aafcf70678984d666952aab6fa6255b018ad1452604914caaeb688";
    private static final String codeVerifier = "f439add3474beb0c415d19e7ba2ef4f1f5ca127b7bbad771799c5e1c";

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