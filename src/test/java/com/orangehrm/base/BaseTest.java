package com.orangehrm.base;

import com.orangehrm.config.ConfigManager;
import com.orangehrm.utils.TokenManager;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static RequestSpecification requestSpec;

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = ConfigManager.getBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .log(LogDetail.ALL)
                .build();

        System.out.println("✅ Base Setup Done!");
    }

    protected RequestSpecification getAuthSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader("Authorization", "Bearer " + TokenManager.getAccessToken())
                .build();
    }
}