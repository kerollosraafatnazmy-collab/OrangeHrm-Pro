package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    private static final String authCode = "def50200589a32bbb6d0a79417bcc46de13cd0156367de87715619e09a65d3e7d86f6c10ac6f029b09d10e4ac7d8f97acab00b5598b466cecfe32d93898a0663baf06b4d4c757b3a3501099e819cb1162c355c68f8d1362132cf8f758a48dd9e40d3f1a095acb439dc5d3d85b97311d66ff20d2171281d8ecd49ebd0b1b14979d2a109735194440d044ccae55924fec63c704c7ad0b9c425ba54503c39a1b30ad6c1fa33b1e6bfb9e0af3ca455963800f9774e53bd57cda7ed7062fc279043ee6e74e1054a55a7110658fe8ff9580de086807148cceaeb1aa9803c84fa5a3e46e032ef06f154aca5dfb4fd024b9c7adbc8c7c70ec51730e89f1a575d4eedc8220a366876cc42ccc56e5a11e7a7b966d67321667625cfac280ad7bdffe5afb0dbb5ffccca36df1ae4735c67bd556de3550f5cf80ed52b75b335665890422a5137c31af7a549f2d49e5f72bab8816891a83f06ee1fb6db90cf3f0683d1a885b5c8dcf8efe68265b18510c3f35d11035e531bff6147279eab9a9867e51f8b620343df32a5c93fb0fad6149c5eeae04f2dcd71a845b1c507";
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