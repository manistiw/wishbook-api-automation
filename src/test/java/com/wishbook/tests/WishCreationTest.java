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
            if ("ADMIN".equals(employee.getRole())|| employee.getId().equals("EMP002")) {
                continue;
            }
            wishesByEmployee.putIfAbsent(employee.getId(), new ArrayList<>());
            for (Map.Entry<String, WishRequest> entry : wishTemplates.entrySet()) {
                WishRequest wishTemplate = entry.getValue();
                
                List<WishRequest> expandedWishes = expandWish(wishTemplate);
                
                for (WishRequest wishRequest : expandedWishes) {
                    createWish(employee, wishRequest);
                }
            }
        }
        Assert.assertTrue(wishRepository.size() > 0);
    }
    
    private List<WishRequest> expandWish(WishRequest template) {
        List<WishRequest> wishes = new ArrayList<>();
        if (template.getShiftIds() != null && !template.getShiftIds().isEmpty()) {
            for (Integer shiftId : template.getShiftIds()) {
                WishRequest wish = new WishRequest();
                wish.setDate(template.getDate());
                wish.setShiftId(shiftId);
                wish.setShiftIds(null);
                wishes.add(wish);
            }
        } else {
            WishRequest wish = new WishRequest();
            wish.setDate(template.getDate());
            wish.setShiftId(template.getShiftId());
            wish.setShiftIds(null);
            wishes.add(wish);
        }
        
        return wishes;
    }
    
    private void createWish(Employee employee, WishRequest wishRequest) {
        WishResponse wishResponse = RestApiClient.request()
            .withToken(employee.getToken())
            .withBody(wishRequest)
            .putAndGet(WISHES_ENDPOINT, WishResponse.class);
        
        // Store WishResponse directly
        wishRepository.put(wishResponse.getWishId(), wishResponse);
        wishesByEmployee.get(employee.getId()).add(wishResponse);
        
        Assert.assertNotNull(wishResponse.getWishId());
    }
}