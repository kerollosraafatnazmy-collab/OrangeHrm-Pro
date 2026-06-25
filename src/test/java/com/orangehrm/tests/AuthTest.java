package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTest extends BaseTest {


    private static final String authCode = "def50200760217e7e6e2493c656c69d8dde983ef6c088883b69e8b45e9af6dfc7a3b34681f7f7def14c2d3608d8a4e68729b928e87c3f7fe642a84dd3e1300080ed08aa927421b9a097e1702e172ddb5ebddafd757ee840c7d4431caa9e3d352e97b21e726e818163d3be40498eace662d9d1243ef14a9eaebbe941d67906c49310b9450dd3df7096c96581c464cadaf425b5ee7f9b7070c85093f1b50b9ca49b6d7d78235ae22fcf0b588b597b02b3636e3484d38c670316b24b81c970944957271105c0e9ce22b6b1b99e44701f31a4b76aed3432b003e6a61ca5d15fbc06c08a42c96549974e10098f3880846ad07dd454223b72bf4be01d743b3a8984d6e94719d9bb5b287cd386caa1da1d4b6a3dd541d4289c934ab567d111baffba9d6bf2052e4d807fc35352f5b0ac004e4fea75b1a56fcbaf63a5c75c690ed95d606bcdd20f798fc2b7420bb2868f610e907abec60dd62c0dd05693513b243d57bf3d779b733cdee39f4bdd2b4aef00efc317897788b242ca50ba2e1f306ee9bdb2e4579fe87207b44a8d046b932bb1c03f720e34dacb014";

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