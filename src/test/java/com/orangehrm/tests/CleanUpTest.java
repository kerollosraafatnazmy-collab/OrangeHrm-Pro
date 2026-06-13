package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class CleanUpTest extends BaseTest {

    @Test(priority = 1, description = "TC-DEL-001: Delete Employment Status")
    public void testDeleteEmploymentStatus() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"ids\":[1]}")
                .when()
                .delete("/api/v2/admin/employment-statuses")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Employment Status Passed!");
    }

    @Test(priority = 2, description = "TC-DEL-002: Delete Leave Request")
    public void testDeleteLeaveRequest() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .delete("/api/v2/leave/employees/leave-requests/" + LeaveTest.leaveRequestId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Leave Request Passed!");
    }

    @Test(priority = 3, description = "TC-DEL-003: Delete Employee")
    public void testDeleteEmployee() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"ids\":[" + EmployeeTest.empNumber + "]}")
                .when()
                .delete("/api/v2/pim/employees")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Employee Passed!");
    }

    @Test(priority = 4, description = "TC-DEL-004: Delete Job Title")
    public void testDeleteJobTitle() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"ids\":[" + EducationTest.educationId + "]}")
                .when()
                .delete("/api/v2/admin/job-titles")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Job Title Passed!");
    }

    @Test(priority = 5, description = "TC-DEL-005: Delete Education")
    public void testDeleteEducation() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"ids\":[" + EducationTest.educationId + "]}")
                .when()
                .delete("/api/v2/admin/educations")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Delete Education Passed!");
    }

    @Test(priority = 6, description = "TC-DEL-006: Revoke Token")
    public void testRevokeToken() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"token\":\"" + com.orangehrm.utils.TokenManager.getAccessToken() + "\"}")
                .when()
                .post("/oauth2/token/revoke")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Revoke Token Passed!");
    }
}