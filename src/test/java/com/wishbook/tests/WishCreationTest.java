package com.wishbook.tests;

import com.wishbook.models.*;
import com.wishbook.utils.PlanningHelper;
import com.wishbook.utils.RestApiClient;
import com.wishbook.utils.TestDataLoader;
import com.wishbook.utils.WishHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class WishCreationTest {
    
    private static final String AUTH_ENDPOINT = "/auth/generate-token";
    
    // Global storage
    public static Map<String, String> employeeTokens = new HashMap<>();
    public static Map<String, String> wishesByEmployee = new HashMap<>();
    
    // Dynamic dates
    private String today;
    private String todayPlus1;
    private String todayPlus2;
    private String todayPlus3;
    
    private List<Employee> employees;
    private List<WishRequest> wishTemplates;
    
    @BeforeClass
    public void setup() throws IOException {
        // Initialize dynamic dates
        today = PlanningHelper.getDateWithOffset(0);
        todayPlus1 = PlanningHelper.getDateWithOffset(1);
        todayPlus2 = PlanningHelper.getDateWithOffset(2);
        todayPlus3 = PlanningHelper.getDateWithOffset(3);
        
        // Load employees
        employees = TestDataLoader.loadEmployees();
        
        // Create wish templates with dynamic dates
        wishTemplates = createWishTemplates();
        
        // Generate tokens
        generateTokensForAllEmployees();
    }
    
    private List<WishRequest> createWishTemplates() {
        List<WishRequest> templates = new ArrayList<>();
        
        // Wish 1: today, shifts 1 and 2
        WishRequest wish1 = new WishRequest();
        wish1.setDate(today);
        wish1.setShiftIds(List.of(1, 2));
        templates.add(wish1);
        
        // Wish 2: today+1, shift 2
        WishRequest wish2 = new WishRequest();
        wish2.setDate(todayPlus1);
        wish2.setShiftIds(List.of(1, 2));
        templates.add(wish2);
        
        // Wish 3: today+2, shift 1
        WishRequest wish3 = new WishRequest();
        wish3.setDate(todayPlus2);
        wish3.setShiftId(1);
        templates.add(wish3);
        
        // Wish 4: today+3, shifts 1 and 2
        WishRequest wish4 = new WishRequest();
        wish4.setDate(todayPlus3);
        wish4.setShiftIds(List.of(1, 2));
        templates.add(wish4);
        
        return templates;
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
            if ("ADMIN".equals(employee.getRole())||employee.getId().equals("EMP002")   ) {
                continue;
            }

            if(employee.getId().equals("EMP001")){
                List<WishRequest> expandedWishes = WishHelper.expandWish(wishTemplates.get(1));
                WishResponse wishResponse = WishHelper.createWish(employee.getToken(), expandedWishes.get(0));
                wishesByEmployee.put(wishResponse.getEmployeeId().toString(), wishResponse.getWishId());
            }
            if(employee.getId().equals("EMP003")){
                List<WishRequest> expandedWishes = WishHelper.expandWish(wishTemplates.get(1));
                WishResponse wishResponse = WishHelper.createWish(employee.getToken(), expandedWishes.get(1));
                wishesByEmployee.put(wishResponse.getEmployeeId().toString(), wishResponse.getWishId());
            }
            if(employee.getId().equals("EMP004")){
                List<WishRequest> expandedWishes = WishHelper.expandWish(wishTemplates.get(1));
                WishResponse wishResponse = WishHelper.createWish(employee.getToken(), expandedWishes.get(0));
                wishesByEmployee.put(wishResponse.getEmployeeId().toString(), wishResponse.getWishId());
            }
            if(employee.getId().equals("EMP006")){
                List<WishRequest> expandedWishes = WishHelper.expandWish(wishTemplates.get(1));
                WishResponse wishResponse = WishHelper.createWish(employee.getToken(), expandedWishes.get(1));
                wishesByEmployee.put(wishResponse.getEmployeeId().toString(), wishResponse.getWishId());
            }
    }
    Assert.assertTrue(wishesByEmployee.size() == 4);
}
    
    @Test(dependsOnMethods = {"testCreateWishesForAllEmployees"})
    public void testEndToEndWorkflow() {
        String adminToken = employeeTokens.get("EMP005");
        Assert.assertNotNull(adminToken);
        Assert.assertTrue(wishesByEmployee.size() == 4);
        // 1. Create shift requirement for todayPlus1 (use future date to avoid locked schedule)
        ShiftRequirementResponse requirementsResponse = PlanningHelper.setDefaultShiftRequirements(adminToken, todayPlus1);
        Assert.assertNotNull(requirementsResponse);
        List<ShiftAssignment> assignments = new ArrayList<>();
        ShiftAssignment assignment1 = new ShiftAssignment(1, new ArrayList<>(wishesByEmployee.values()).subList(0,2));
        ShiftAssignment assignment2 = new ShiftAssignment(2, new ArrayList<>(wishesByEmployee.values()).subList(2,4));
        assignments.add(assignment1);
        assignments.add(assignment2);
        PlanningResponse planning = PlanningHelper.createPlanning(adminToken, todayPlus1, assignments);
        Assert.assertNotNull(planning);
    }
}