package com.wishbook.tests;

import com.wishbook.models.Employee;
import com.wishbook.models.WishRequest;
import com.wishbook.models.WishResponse;
import com.wishbook.utils.RestApiClient;
import com.wishbook.utils.TestDataLoader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishCreationTest {

    private static final String WISHES_ENDPOINT = "/wishes";
    //private static final ObjectMapper objectMapper = new ObjectMapper();

    private List<Employee> employees;
    private Map<String, WishRequest> wishTemplates;

    @BeforeClass
    public void setup() throws IOException {
        // Load test data
        employees = TestDataLoader.loadEmployees();
        wishTemplates = TestDataLoader.loadWishTemplates();

        System.out.println("Loaded " + employees.size() + " employees");
        System.out.println("Loaded " + wishTemplates.size() + " wish templates");
    }

    @Test(description = "Create 4 wishes for all employees and validate responses")
    public void testCreateWishesForAllEmployees() throws IOException {

        List<WishCreationResult> allResults = new ArrayList<>();

        // Iterate through each employee
        for (Employee employee : employees) {

            // Skip ADMIN role as they typically don't create wishes
            if ("ADMIN".equals(employee.getRole())) {
                System.out.println("Skipping ADMIN user: " + employee.getName());
                continue;
            }

            System.out.println("\n========================================");
            System.out.println("Creating wishes for: " + employee.getName() + " (" + employee.getId() + ")");
            System.out.println("========================================");

            // Create 4 wishes for this employee
            int wishCount = 1;
            for (Map.Entry<String, WishRequest> entry : wishTemplates.entrySet()) {

                WishRequest wishRequest = entry.getValue();
                String wishKey = entry.getKey();

                System.out.println("\nWish " + wishCount + " for " + employee.getName());
                System.out.println("Request: " + TestDataLoader.toJson(wishRequest));

                // Make PUT request to create wish
                Response response = RestApiClient.put(
                        WISHES_ENDPOINT,
                        wishRequest,
                        employee.getToken()
                );

                // Log response
                System.out.println("Response Status: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody().asString());

                // Validate status code
                Assert.assertTrue(
                        response.getStatusCode() == 200 || response.getStatusCode() == 201,
                        "Expected status 200 or 201, but got: " + response.getStatusCode()
                );

                // Deserialize response
                WishResponse actualResponse = response.as(WishResponse.class);

                // Build expected response
                WishResponse expectedResponse = WishResponse.builder()
                        .employeeId(employee.getId())
                        .date(wishRequest.getDate())
                        .shiftId(wishRequest.getShiftId())
                        .isActive(true)
                        .build();

                // Validate core fields (excluding wishId, createdAt, updatedAt as they're dynamic)
                Assert.assertEquals(actualResponse.getEmployeeId(), expectedResponse.getEmployeeId(),
                        "Employee ID mismatch");
                Assert.assertEquals(actualResponse.getDate(), expectedResponse.getDate(),
                        "Date mismatch");
                Assert.assertEquals(actualResponse.getShiftId(), expectedResponse.getShiftId(),
                        "Shift ID mismatch");
                Assert.assertEquals(actualResponse.getIsActive(), expectedResponse.getIsActive(),
                        "IsActive status mismatch");

                // Validate dynamic fields exist
                Assert.assertNotNull(actualResponse.getWishId(), "Wish ID should not be null");
                Assert.assertNotNull(actualResponse.getCreatedAt(), "Created timestamp should not be null");

                System.out.println("✓ Wish created and validated successfully");

                // Store result
                WishCreationResult result = new WishCreationResult(
                        employee,
                        wishKey,
                        wishRequest,
                        actualResponse
                );
                allResults.add(result);

                wishCount++;
            }
        }

        // Summary
        System.out.println("\n\n========================================");
        System.out.println("TEST SUMMARY");
        System.out.println("========================================");
        System.out.println("Total wishes created: " + allResults.size());
        System.out.println("Expected wishes per employee: 4");
        System.out.println("Number of employees processed: " + (allResults.size() / 4));
        System.out.println("All wishes created and validated successfully! ✓");

        // Validate total count
        int expectedEmployees = employees.size() - 1; // Exclude ADMIN
        int expectedTotalWishes = expectedEmployees * 4;
        Assert.assertEquals(allResults.size(), expectedTotalWishes,
                "Total wishes created should match expected count");
    }

    /**
     * Helper class to store wish creation results
     */
    private static class WishCreationResult {
        Employee employee;
        String wishKey;
        WishRequest request;
        WishResponse response;

        WishCreationResult(Employee employee, String wishKey, WishRequest request, WishResponse response) {
            this.employee = employee;
            this.wishKey = wishKey;
            this.request = request;
            this.response = response;
        }
    }
}