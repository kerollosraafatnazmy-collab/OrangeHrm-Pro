package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EducationTest extends BaseTest {

    public static int educationId;
    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "TC-EDU-001: Create Education Record")
    public void testCreateEducation() {

        Response response = RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"name\":\"Bachelor of Science\"}")
                .when()
                .post("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        educationId = response.jsonPath().getInt("data.id");
        System.out.println("✅ Education Created! ID: " + educationId);
        Assert.assertTrue(educationId > 0);
    }

    @Test(priority = 2, description = "TC-EDU-002: Get Education by ID",
            dependsOnMethods = "testCreateEducation")
    public void testGetEducationById() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/admin/educations/" + educationId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Education by ID Passed!");
    }

    @Test(priority = 3, description = "TC-EDU-003: Update Education Record",
            dependsOnMethods = "testCreateEducation")
    public void testUpdateEducation() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"name\":\"Master of Science\"}")
                .when()
                .put("/api/v2/admin/educations/" + educationId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Update Education Passed!");
    }

    @Test(priority = 4, description = "TC-EDU-004: List All Education Records")
    public void testListEducations() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ List Educations Passed!");
    }

    @Test(priority = 5, description = "TC-EDU-005: Delete Education Record",
            dependsOnMethods = "testCreateEducation")
    public void testDeleteEducation() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"ids\":[" + educationId + "]}")
                .when()
                .delete("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Education Passed!");
    }

    // ==================== NEGATIVE TESTS ====================

    @Test(priority = 6, description = "TC-EDU-001-N1: Create Education - Missing name")
    public void testCreateEducationMissingName() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{}")
                .when()
                .post("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Create Education Missing Name Passed!");
    }

    @Test(priority = 7, description = "TC-EDU-001-N2: Create Education - No Token")
    public void testCreateEducationNoToken() {

        RestAssured
                .given()
                .spec(requestSpec)
                .body("{\"name\":\"Bachelor of Science\"}")
                .when()
                .post("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(401);

        System.out.println("✅ Create Education No Token Passed!");
    }

    @Test(priority = 8, description = "TC-EDU-002-N1: Get Education - Invalid ID")
    public void testGetEducationInvalidId() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/admin/educations/99999")
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("✅ Get Education Invalid ID Passed!");
    }

    @Test(priority = 9, description = "TC-EDU-003-N1: Update Education - Invalid ID")
    public void testUpdateEducationInvalidId() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"name\":\"PhD\"}")
                .when()
                .put("/api/v2/admin/educations/99999")
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("✅ Update Education Invalid ID Passed!");
    }

    @Test(priority = 10, description = "TC-EDU-003-N2: Update Education - Missing name")
    public void testUpdateEducationMissingName() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{}")
                .when()
                .put("/api/v2/admin/educations/" + educationId)
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Update Education Missing Name Passed!");
    }
}