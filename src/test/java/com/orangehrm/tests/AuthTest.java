package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {

    private static final String authCode = "def5020054962987685ca9a564ca504c85621c82101f5043e5dcb111d975b57a06389d51043fe8ca1ab8f2df3f9f91b7218c7498000e64d9b91e612efe2665b126592181d363ba1c2145c18b6fe3ab5403f7c8801b32cd7a5df95eb9c51f513bfb08c3c80a7c5ddbe590137ed86f24ac3d7b78bfa5a8cbace3f92584f83e2ecb5522f6c8ac256511ad21d6b6f20b23ce70647faa22a1ca907ae12722d74a0f89878d5218733c2949055231846d436fa35d9209130d0c6fc936617c0e61e1dc184e8e4eef0303cea9a1e04ad4aee84a7316c40f427264bb5816d3d683b34ac5a147df437e6883497a773e433b4948a8d839ea98ada68edbf4f08bc797a77ea648a05687d3d0e95d0f19f14a0cb5dc6530ab9e8c8c486f859643bbe2dfa80e3de14b76a75472216624f11869ba69556160795ac6ad547b4f681b530e8ee952c44257c30bbf7048590fa898433840ae592e9717df97dd4dedeffd2a8f8cc08006116185cc4239c428547f1427c55f9c7d5235c28307cba93fc49fd330adaf0ba7a3061fffe0320823c433dbdddee466b2fd6465ba039f22";
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