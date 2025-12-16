package com.wishbook.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wishbook.models.Employee;
import com.wishbook.models.WishRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TestDataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "/testdata/";

    /**
     * Load employees from JSON file
     */
    public static List<Employee> loadEmployees() throws IOException {
        String filePath = TEST_DATA_PATH + "employee.json";
        InputStream inputStream = TestDataLoader.class.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        // Use TypeReference for complex nested types
        Map<String, List<Employee>> data = objectMapper.readValue(
                inputStream,
                new TypeReference<Map<String, List<Employee>>>() {}
        );

        return data.get("employees");
    }

    /**
     * Load wish templates from JSON file
     */
    public static Map<String, WishRequest> loadWishTemplates() throws IOException {
        String filePath = TEST_DATA_PATH + "wish_templates.json";
        InputStream inputStream = TestDataLoader.class.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IOException("File not found: " + filePath);
        }

        // Use TypeReference for Map types
        return objectMapper.readValue(
                inputStream,
                new TypeReference<Map<String, WishRequest>>() {}
        );
    }
}