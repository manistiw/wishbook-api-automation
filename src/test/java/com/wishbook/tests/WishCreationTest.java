package com.wishbook.tests;

import com.wishbook.models.Employee;
import com.wishbook.models.WishRequest;
import com.wishbook.models.WishResponse;
import com.wishbook.utils.RestApiClient;
import com.wishbook.utils.TestDataLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishCreationTest {
    
    private static final String AUTH_ENDPOINT = "/auth/generate-token";
    private static final String WISHES_ENDPOINT = "/wishes";
    
    // Global storage
    public static Map<String, String> employeeTokens = new HashMap<>();
    public static Map<String, WishData> wishRepository = new HashMap<>();
    public static Map<String, List<WishData>> wishesByEmployee = new HashMap<>();
    
    private List<Employee> employees;
    private Map<String, WishRequest> wishTemplates;
    
    @BeforeClass
    public void setup() throws IOException {
        employees = TestDataLoader.loadEmployees();
        wishTemplates = TestDataLoader.loadWishTemplates();
        generateTokensForAllEmployees();
    }
    
    private void generateTokensForAllEmployees() {
        for (Employee employee : employees) {
            // Generate token using generic client
            String token = RestApiClient.generateToken(
                AUTH_ENDPOINT, 
                employee.getId(), 
                employee.getRole()
            );
            
            employee.setToken(token);
            employeeTokens.put(employee.getId(), token);
        }
    }
    
    @Test
    public void testCreateWishesForAllEmployees() throws IOException {
        for (Employee employee : employees) {
            if ("ADMIN".equals(employee.getRole())||employee.getId().equals("EMP002")) {
                continue;
            }
            
            wishesByEmployee.putIfAbsent(employee.getId(), new ArrayList<>());
            
            for (Map.Entry<String, WishRequest> entry : wishTemplates.entrySet()) {
                WishRequest wishRequest = entry.getValue();
                System.out.println("Creating wish for employee: " + employee.getId() + " with data: " + wishRequest);
                // Create wish using generic client
                WishResponse wishResponse = RestApiClient.request()
                    .withToken(employee.getToken())
                    .withBody(wishRequest)
                    .putAndGet(WISHES_ENDPOINT, WishResponse.class);
                
                // Store wish globally
                WishData wishData = new WishData(
                    wishResponse.getWishId(),
                    employee.getId(),
                    wishResponse.getDate(),
                    wishResponse.getShiftId()
                );
                
                wishRepository.put(wishResponse.getWishId(), wishData);
                wishesByEmployee.get(employee.getId()).add(wishData);
                
                // Validate
                Assert.assertEquals(wishResponse.getEmployeeId(), employee.getId());
                Assert.assertEquals(wishResponse.getShiftId(), wishRequest.getShiftId());
                Assert.assertNotNull(wishResponse.getWishId());
            }
        }
    }
    
    public static class WishData {
        public String wishId;
        public String employeeId;
        public String date;
        public Integer shiftId;
        
        public WishData(String wishId, String employeeId, String date, Integer shiftId) {
            this.wishId = wishId;
            this.employeeId = employeeId;
            this.date = date;
            this.shiftId = shiftId;
        }
    }
}