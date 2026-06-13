package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    private static final String authCode = "def50200ab5dcb49717c8e94dfeb3072cbebfed17a77d30abcbe9aa0ef549dc4c02aacdbef6f2bf0ba29886cf18b5db5ece5e08bb3d529cd2b31059aa4088014100430067e9bd32ace76d47086cb3376008fd103638f610898bb323a9ddc05267f8c10959269f610b7f377876a492092bc18db29b4860eadd27dd1c4f68d2318f68fbb97c776feb27c66daadcf293ef49822e3cc73c3b6b1a2266306f78be8b713406fb29a79a1ef1e850de7b7f3c4f4da2b4badfde14b25e50a285a8f3e986966bf04b024eb04a030d988bb3c0d051ef8ae3178dadb05ccc3f928200e2e1e570edefa99295a8ef00f7acd5a8ff11cf0356a8cc2b0e5614da1a39e20cb5206d0d0f6d4ff64525fada2830a9da904c63674e5f00267ead32390e5561b021e7a15f37fdae137b45f0fc0da9dcbc176b0375ef6b035178d2218ce98d60c63bf6e72a8bf0bbe35748b52b4d93c8454462c815cf70d56aae0d59116e48b3d0ea592cc20fb08051ede4cfefddd90cf11d78396745783e91be4b3216bcdb06b07cb57fdf483a684aa856455b2e1ec9a3e467a522dadfa5cdbec";
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