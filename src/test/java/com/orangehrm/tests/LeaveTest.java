package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LeaveTest extends BaseTest {

    private static int leaveTypeId;
    public static int leaveRequestId;
    private static int entitlementId;

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "TC-LVT-001: Create Leave Type")
    public void testCreateLeaveType() {

        Response response = RestAssured
                .given()
                .spec(getAuthSpec())

                .body("{\"name\":\"Annual Leave\",\"situational\":false}")
                .when()
                .post("/api/v2/leave/leave-types")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        leaveTypeId = response.jsonPath().getInt("data.id");
        System.out.println("✅ Leave Type Created! ID: " + leaveTypeId);
        Assert.assertTrue(leaveTypeId > 0);
    }

    @Test(priority = 2, description = "TC-LVT-002: Get Leave Type by ID",
            dependsOnMethods = "testCreateLeaveType")
    public void testGetLeaveTypeById() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/leave/leave-types/" + leaveTypeId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Leave Type by ID Passed!");
    }

    @Test(priority = 3, description = "TC-LVT-003: Update Leave Type",
            dependsOnMethods = "testCreateLeaveType")
    public void testUpdateLeaveType() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"name\":\"Annual Leave Updated\",\"entitlementType\":1,\"situational\":false}")
                .when()
                .put("/api/v2/leave/leave-types/" + leaveTypeId)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Update Leave Type Passed!");
    }

    @Test(priority = 4, description = "TC-LVE-001: Assign Leave Entitlement",
            dependsOnMethods = "testCreateLeaveType")
    public void testAssignLeaveEntitlement() {

        Response response = RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"empNumber\":1,\"leaveTypeId\":" + leaveTypeId + ",\"entitlement\":21,\"fromDate\":\"2026-01-01\",\"toDate\":\"2026-12-31\"}")
                .when()
                .post("/api/v2/leave/leave-entitlements")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        entitlementId = response.jsonPath().getInt("data.id");
        System.out.println("✅ Leave Entitlement Assigned! ID: " + entitlementId);
    }

    @Test(priority = 5, description = "TC-LVR-001: Create Leave Request",
            dependsOnMethods = "testAssignLeaveEntitlement")
    public void testCreateLeaveRequest() {

        Response response = RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"empNumber\":1,\"leaveTypeId\":" + leaveTypeId + ",\"fromDate\":\"2026-07-01\",\"toDate\":\"2026-07-01\",\"fromDateLeaveHours\":\"0\",\"toDateLeaveHours\":\"0\",\"comment\":\"Test Leave\"}")
                .when()
                .post("/api/v2/leave/employees/leave-requests")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        leaveRequestId = response.jsonPath().getInt("data.id");
        System.out.println("✅ Leave Request Created! ID: " + leaveRequestId);
    }

    @Test(priority = 6, description = "TC-LVB-001: Get Leave Balance",
            dependsOnMethods = "testCreateLeaveType")
    public void testGetLeaveBalance() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/leave/leave-balance/leave-type/" + leaveTypeId + "?empNumber=1")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Leave Balance Passed!");
    }

    // ==================== NEGATIVE TESTS ====================

    @Test(priority = 7, description = "TC-LVT-001-N1: Create Leave Type - Missing name")
    public void testCreateLeaveTypeMissingName() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{}")
                .when()
                .post("/api/v2/leave/leave-types")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Create Leave Type Missing Name Passed!");
    }

    @Test(priority = 8, description = "TC-LVT-001-N2: Create Leave Type - No Token")
    public void testCreateLeaveTypeNoToken() {

        RestAssured
                .given()
                .spec(requestSpec)
                .body("{\"name\":\"Annual Leave\"}")
                .when()
                .post("/api/v2/leave/leave-types")
                .then()
                .log().all()
                .statusCode(401);

        System.out.println("✅ Create Leave Type No Token Passed!");
    }

    @Test(priority = 9, description = "TC-LVT-002-N1: Get Leave Type - Invalid ID")
    public void testGetLeaveTypeInvalidId() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/leave/leave-types/99999")
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("✅ Get Leave Type Invalid ID Passed!");
    }

    @Test(priority = 10, description = "TC-LVE-001-N1: Assign Entitlement - Invalid empNumber")
    public void testAssignEntitlementInvalidEmp() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"empNumber\":99999,\"leaveTypeId\":" + leaveTypeId + ",\"entitlement\":21,\"fromDate\":\"2026-01-01\",\"toDate\":\"2026-12-31\"}")
                .when()
                .post("/api/v2/leave/leave-entitlements")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Assign Entitlement Invalid Emp Passed!");
    }

    @Test(priority = 11, description = "TC-LVR-002-N1: Approve Leave - Invalid ID")
    public void testApproveLeaveInvalidId() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"action\":\"APPROVE\"}")
                .when()
                .put("/api/v2/leave/employees/leave-requests/99999")
                .then()
                .log().all()
                .statusCode(404);

        System.out.println("✅ Approve Leave Invalid ID Passed!");
    }
}