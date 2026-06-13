package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {



    private static final String authCode = "def50200e3751ad7029da28acf622ca9681a7e3db54b3f0220c90b31b1a0f57683c2b83c2e8619ee681b9171862889eb3da32cd660d5a022d4aec0f40ad7b16ac9e23c713ec4f936e09e6fc9f6d84f91c30f9a794526013dda4f5db8c4bbb34b552bc665d41efa65af2cef525ed762ed88c522736ca84eac4d779c915569d694b050902fd36cd0d18064daffc8a2b9029c34e66e526387c11ab3a0490651b5e4a7ab5eca46505f5f8faf4c7cb3af8cfea98fdfb2adee58c12164b2067c1273b65bf49e9060762ee5b6ebf528787dfcdbdce9fbab21632b9fe607498c9b539c64ac51d31b3117a7c8cd05607987899a64b2aad4d6303e27790e9ea428ebe6c8d33e1047979dd80efc2a0a30db65177858adf3be8a8176b22171cbb9d6b01cd3d336381c554c1dc6bf0ba8a25a73d52fd3b2572f12fa5406dc9eb9cc060f060b7c852507e626017e5af0af32ae3da0eaaa1111b005c9a1363aaec37368ff014aab8a69fcae292df0f2ab027e8061ebddcff8d4a999250fd54eea5d814d6399002c24639cad07dc4b3628185f55c7f50b88f28038ac6d8b";
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