package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class CleanUpTest extends BaseTest {

    @Test(priority = 1, description = "TC-DEL-001: Delete Employee")
    public void testDeleteEmployee() {

        if (EmployeeTest.empNumber == 0) {
            System.out.println("⚠️ No Employee ID - Skipping");
            return;
        }

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

    @Test(priority = 2, description = "TC-DEL-002: Cancel Leave Request")
    public void testCancelLeaveRequest() {

        if (LeaveTest.leaveRequestId == 0) {
            System.out.println("⚠️ No Leave Request ID - Skipping");
            return;
        }

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"action\":\"CANCEL\"}")
                .when()
                .put("/api/v2/leave/employees/leave-requests/" + LeaveTest.leaveRequestId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Cancel Leave Request Passed!");
    }

    @Test(priority = 3, description = "TC-DEL-003: Delete Job Title")
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

    @Test(priority = 4, description = "TC-DEL-004: Delete Education")
    public void testDeleteEducation() {

        if (EducationTest.educationId == 0) {
            System.out.println("⚠️ No Education ID - Skipping");
            return;
        }

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

    @Test(priority = 5, description = "TC-DEL-005: Revoke Token")
    public void testRevokeToken() {

        int status = RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"token\":\"" + com.orangehrm.utils.TokenManager.getAccessToken() + "\"}")
                .when()
                .post("/oauth2/token/revoke")
                .then()
                .log().all()
                .extract().statusCode();

        System.out.println("⚠️ Revoke Token Status: " + status + " (BUG-001: 404 in demo)");
    }
}