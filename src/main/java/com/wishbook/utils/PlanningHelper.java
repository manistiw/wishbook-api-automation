package com.wishbook.utils;

import com.wishbook.models.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanningHelper {
    
    private static final String PLANNING_ENDPOINT = "/planning/assign";
    private static final String SHIFT_REQUIREMENTS_ENDPOINT = "/shift-requirements";
    
    /**
     * Set shift requirements for a date
     */
    public static ShiftRequirementResponse setShiftRequirements(String token, String date, List<ShiftRequirement> requirements) {
        ShiftRequirementRequest request = new ShiftRequirementRequest(requirements);
        
        return RestApiClient.request()
            .withToken(token)
            .withBody(request)
            .postAndGet(SHIFT_REQUIREMENTS_ENDPOINT + "/" + date, ShiftRequirementResponse.class);
    }
    
    /**
     * Set default shift requirements (2 staff per shift for shifts 1 and 2)
     */
    public static ShiftRequirementResponse setDefaultShiftRequirements(String token, String date) {
        List<ShiftRequirement> requirements = List.of(
            new ShiftRequirement(1, 2),
            new ShiftRequirement(2, 2)
        );
        
        return setShiftRequirements(token, date, requirements);
    }
    
    /**
     * Create a planning assignment
     */
    public static PlanningResponse createPlanning(String token, PlanningRequest request) {
        return RestApiClient.request()
            .withToken(token)
            .withBody(request)
            .postAndGet(PLANNING_ENDPOINT, PlanningResponse.class);
    }
    
    /**
     * Create planning with automatic effectiveMoment (midnight UTC)
     */
    public static PlanningResponse createPlanning(String token, String date, List<ShiftAssignment> assignments) {
        String effectiveMoment = toMidnightUtc(date);
        PlanningRequest request = new PlanningRequest(date, effectiveMoment, assignments);
        return createPlanning(token, request);
    }
    
    /**
     * Group wishes by shift ID
     * Useful for creating assignments from a list of wishes
     */
    public static Map<Integer, List<String>> groupWishesByShift(List<WishResponse> wishes) {
        Map<Integer, List<String>> grouped = new HashMap<>();
        
        for (WishResponse wish : wishes) {
            grouped.computeIfAbsent(wish.getShiftId(), k -> new ArrayList<>())
                   .add(wish.getWishId());
        }
        
        return grouped;
    }
    
    /**
     * Create shift assignments from grouped wishes
     * Limits assignments based on shift requirements (if provided)
     */
    public static List<ShiftAssignment> createAssignments(Map<Integer, List<String>> groupedWishes) {
        return createAssignments(groupedWishes, null);
    }
    
    /**
     * Create shift assignments from grouped wishes with requirements limit
     */
    public static List<ShiftAssignment> createAssignments(Map<Integer, List<String>> groupedWishes, List<ShiftRequirement> requirements) {
        List<ShiftAssignment> assignments = new ArrayList<>();
        
        // Create a map of requirements for quick lookup
        Map<Integer, Integer> requirementMap = new HashMap<>();
        if (requirements != null) {
            for (ShiftRequirement req : requirements) {
                requirementMap.put(req.getShiftId(), req.getRequiredCount());
            }
        }
        
        for (Map.Entry<Integer, List<String>> entry : groupedWishes.entrySet()) {
            Integer shiftId = entry.getKey();
            List<String> wishIds = entry.getValue();
            
            // Limit wish IDs based on requirements
            List<String> limitedWishIds = wishIds;
            if (requirementMap.containsKey(shiftId)) {
                int requiredCount = requirementMap.get(shiftId);
                if (wishIds.size() > requiredCount) {
                    limitedWishIds = wishIds.subList(0, requiredCount);
                }
            }
            
            ShiftAssignment assignment = new ShiftAssignment(shiftId, limitedWishIds);
            assignments.add(assignment);
        }
        
        return assignments;
    }
    
    /**
     * Create planning from a list of wishes
     * Automatically groups wishes by shift and creates assignments
     * Limits assignments to 2 employees per shift (default)
     * Ensures no employee is assigned to multiple shifts on the same day
     */
    public static PlanningResponse createPlanningFromWishes(String token, String date, List<WishResponse> wishes) {
        // Default requirements: 2 per shift
        List<ShiftRequirement> defaultRequirements = List.of(
            new ShiftRequirement(1, 2),
            new ShiftRequirement(2, 2)
        );
        
        return createPlanningFromWishes(token, date, wishes, defaultRequirements);
    }
    
    /**
     * Create planning from a list of wishes with custom requirements
     * Ensures no employee is assigned to multiple shifts on the same day
     */
    public static PlanningResponse createPlanningFromWishes(String token, String date, List<WishResponse> wishes, List<ShiftRequirement> requirements) {
        Map<Integer, List<String>> grouped = groupWishesByShift(wishes);
        
        // Remove duplicate employees across shifts
        Map<Integer, List<String>> deduplicated = deduplicateEmployeesAcrossShifts(grouped, wishes);
        
        List<ShiftAssignment> assignments = createAssignments(deduplicated, requirements);
        
        return createPlanning(token, date, assignments);
    }
    
    /**
     * Ensure each employee is only assigned to one shift per day
     * Priority: Shift 1 first, then Shift 2, etc.
     */
    private static Map<Integer, List<String>> deduplicateEmployeesAcrossShifts(
            Map<Integer, List<String>> groupedWishes, 
            List<WishResponse> allWishes) {
        
        Map<Integer, List<String>> result = new HashMap<>();
        Map<String, String> assignedEmployees = new HashMap<>(); // wishId -> employeeId
        
        // Create wishId to employeeId mapping
        Map<String, String> wishToEmployee = new HashMap<>();
        for (WishResponse wish : allWishes) {
            wishToEmployee.put(wish.getWishId(), wish.getEmployeeId());
        }
        
        // Process shifts in order (1, 2, 3, ...)
        List<Integer> sortedShifts = new ArrayList<>(groupedWishes.keySet());
        sortedShifts.sort(Integer::compareTo);
        
        for (Integer shiftId : sortedShifts) {
            List<String> wishIds = groupedWishes.get(shiftId);
            List<String> uniqueWishIds = new ArrayList<>();
            
            for (String wishId : wishIds) {
                String employeeId = wishToEmployee.get(wishId);
                
                // Only add if employee not already assigned to another shift
                if (!assignedEmployees.containsValue(employeeId)) {
                    uniqueWishIds.add(wishId);
                    assignedEmployees.put(wishId, employeeId);
                }
            }
            
            result.put(shiftId, uniqueWishIds);
        }
        
        return result;
    }
    
    /**
     * Filter wishes by date
     */
    public static List<WishResponse> filterWishesByDate(List<WishResponse> wishes, String date) {
        return wishes.stream()
            .filter(w -> w.getDate() != null && w.getDate().startsWith(date))
            .collect(Collectors.toList());
    }
    
    /**
     * Convert date to midnight UTC format
     * Example: "2025-12-16" -> "2025-12-16T00:00:00.000Z"
     */
    public static String toMidnightUtc(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.atStartOfDay(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
    
    /**
     * Get today's date in ISO format
     */
    public static String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    /**
     * Get date with offset (today+1, today+2, etc.)
     */
    public static String getDateWithOffset(int daysOffset) {
        return LocalDate.now().plusDays(daysOffset).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    /**
     * Get today's midnight UTC
     */
    public static String getTodayMidnightUtc() {
        return LocalDate.now().atStartOfDay(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }
}