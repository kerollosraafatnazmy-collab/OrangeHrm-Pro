package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {


    private static final String authCode = "def50200d5985583a4f6870ca110d8c7ec1e2730e90f39c652f77cc2b9a3471d2f9b47d61b690d19494cf47c3936cf76bdb0a8b5ac5477cd6ef1fe59c93097fc555ac1e58a1747fec079078e56e496b3b68651ff248b3377445141147d5a0a84e8b22160a280e717c8a01eea76b21e206f0458f36f3451393b9965fc0b7123dfea88a653acc1390408d637416e2d8906fb3f629d222815c49b9f99f25aa283c689eaf1f672a3cb5fdaba60b2958aa4b3bbc61a831ffc01d760b6933e6e3276938ba045ac2dbdb4beab468f0161ff39691e68ef834b83678797dd473731caffbaf96aaf3ab8636685d99473b459a55bedd78c454d4e54420455d97a1e24fe7f2f363414cc6d0f2dcdbcff51775445ead685ceb70d359a2669e8d30c7151bcd7ec9be58bdb31d5a9d5bf6f5546c68b326a85ab42919854c444f2995b91a99821b8d2cc728a0191990d7f5871d241d82a92cd4dc5aac03eb25dbbfa8669d5b4716ba813b9e8a702fd273331c19ed3527ce3b8bb3aa9a48e8aef14a4321af4279aac525d9118c62a6f0afb02140532b05e623c365a842cf4";

    private static final String codeVerifier = "6e9ba3be48c5ac5e874b5153861158ded3452ca732bb8837ffe4ef2e";

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