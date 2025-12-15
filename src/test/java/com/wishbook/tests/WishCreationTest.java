package com.wishbook.tests;

import com.wishbook.models.Employee;
import com.wishbook.models.WishRequest;
import com.wishbook.models.WishResponse;
import com.wishbook.utils.RestApiClient;
import com.wishbook.utils.TestDataLoader;
import com.wishbook.utils.WishHelper;
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
    
    // Global storage
    public static Map<String, String> employeeTokens = new HashMap<>();
    public static Map<String, WishResponse> wishRepository = new HashMap<>();
    public static Map<String, List<WishResponse>> wishesByEmployee = new HashMap<>();
    
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
            for (WishRequest wishTemplate : wishTemplates.values()) {
                List<WishRequest> expandedWishes = WishHelper.expandWish(wishTemplate);
                for (WishRequest wishRequest : expandedWishes) {
                    WishResponse wishResponse = WishHelper.createWish(employee.getToken(), wishRequest);
                    wishRepository.put(wishResponse.getWishId(), wishResponse);
                    wishesByEmployee.get(employee.getId()).add(wishResponse);
                    Assert.assertNotNull(wishResponse.getWishId());
                }
            }
        }
    }
}