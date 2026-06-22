package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmployeeTest extends BaseTest {

    public static int empNumber;

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "TC-PIM-001: Create Employee")
    public void testCreateEmployee() {

        Response response = RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"firstName\":\"Kerollos\",\"lastName\":\"Raafat\",\"middleName\":\"\"}")
                .when()
                .post("/api/v2/pim/employees")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        empNumber = response.jsonPath().getInt("data.empNumber");
        System.out.println("✅ Employee Created! empNumber: " + empNumber);
        Assert.assertTrue(empNumber > 0);
    }

    @Test(priority = 2, description = "TC-PIM-002: Get Employee by ID",
            dependsOnMethods = "testCreateEmployee")
    public void testGetEmployeeById() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/pim/employees/" + empNumber)
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Employee by ID Passed!");
    }

    @Test(priority = 3, description = "TC-PIM-003: Get Personal Details",
            dependsOnMethods = "testCreateEmployee")
    public void testGetPersonalDetails() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/pim/employees/" + empNumber + "/personal-details")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Personal Details Passed!");
    }

    @Test(priority = 4, description = "TC-PIM-004: Update Personal Details",
            dependsOnMethods = "testCreateEmployee")
    public void testUpdatePersonalDetails() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"firstName\":\"Kerollos\",\"lastName\":\"Raafat\",\"maritalStatus\":\"single\",\"birthday\":\"1998-01-01\"}")
                .when()
                .put("/api/v2/pim/employees/" + empNumber + "/personal-details")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Update Personal Details Passed!");
    }

    @Test(priority = 5, description = "TC-PIM-005: Get Job Details",
            dependsOnMethods = "testCreateEmployee")
    public void testGetJobDetails() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/pim/employees/" + empNumber + "/job-details")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Job Details Passed!");
    }

    @Test(priority = 6, description = "TC-PIM-009: Add Salary Component",
            dependsOnMethods = "testCreateEmployee")
    public void testAddSalaryComponent() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"salaryComponent\":\"Base Salary\",\"currencyId\":\"USD\",\"salaryAmount\":\"5000\",\"comment\":\"Monthly\"}")
                .when()
                .post("/api/v2/pim/employees/" + empNumber + "/salary-components")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Add Salary Component Passed!");
    }

    @Test(priority = 7, description = "TC-PIM-010: Get Employee Salary",
            dependsOnMethods = "testCreateEmployee")
    public void testGetEmployeeSalary() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/pim/employees/" + empNumber + "/salary-components")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("✅ Get Employee Salary Passed!");
    }

    // ==================== NEGATIVE TESTS ====================

    @Test(priority = 8, description = "TC-PIM-001-N1: Create Employee - Missing firstName")
    public void testCreateEmployeeMissingFirstName() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"lastName\":\"Raafat\"}")
                .when()
                .post("/api/v2/pim/employees")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Create Employee Missing FirstName Passed!");
    }

    @Test(priority = 9, description = "TC-PIM-001-N2: Create Employee - No Token")
    public void testCreateEmployeeNoToken() {

        RestAssured
                .given()
                .spec(requestSpec)
                .body("{\"firstName\":\"Kerollos\",\"lastName\":\"Raafat\"}")
                .when()
                .post("/api/v2/pim/employees")
                .then()
                .log().all()
                .statusCode(401);

        System.out.println("✅ Create Employee No Token Passed!");
    }

    @Test(priority = 10, description = "TC-PIM-002-N1: Get Employee - Invalid empNumber")
    public void testGetEmployeeInvalidId() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .when()
                .get("/api/v2/pim/employees/99999")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Get Employee Invalid ID Passed!");
    }

    @Test(priority = 11, description = "TC-PIM-004-N1: Update Personal Details - Invalid Date")
    public void testUpdatePersonalDetailsInvalidDate() {

        RestAssured
                .given()
                .spec(getAuthSpec())
                .body("{\"firstName\":\"Kerollos\",\"lastName\":\"Raafat\",\"birthday\":\"not-a-date\"}")
                .when()
                .put("/api/v2/pim/employees/" + empNumber + "/personal-details")
                .then()
                .log().all()
                .statusCode(422);

        System.out.println("✅ Update Personal Details Invalid Date Passed!");
    }
}