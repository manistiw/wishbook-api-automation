# Wishbook API Test Automation

A comprehensive REST API test automation suite for the Wishbook scheduling system using RestAssured and TestNG.

## Overview

This project implements an **End-to-End (E2E) workflow** that demonstrates the complete cycle of wish creation and schedule planning:

1. **Employee Authentication** - Generate tokens for employees
2. **Wish Creation** - Employees create shift wishes
3. **Shift Requirements** - Admin sets shift requirements for a date
4. **Planning & Assignments** - Admin creates final schedule assignments from employee wishes

## E2E Workflow Description

### Step 1: Employee Authentication
- Generate authentication tokens for 4 employees (EMP001, EMP003, EMP004, EMP006)
- Admin token (EMP005) is generated for planning operations
- Tokens are stored and reused throughout the test

### Step 2: Wish Creation (`testCreateWishesForAllEmployees`)
Each of 4 employees creates shift wishes for tomorrow (todayPlus1):

**EMP001**: Creates a wish for Shift 1
- Date: Tomorrow (todayPlus1)
- Shift: 1

**EMP003**: Creates a wish for Shift 2
- Date: Tomorrow (todayPlus1)
- Shift: 2

**EMP004**: Creates a wish for Shift 1
- Date: Tomorrow (todayPlus1)
- Shift: 1

**EMP006**: Creates a wish for Shift 2
- Date: Tomorrow (todayPlus1)
- Shift: 2

**Result**: 4 wishes created and stored in `wishesByEmployee` map

### Step 3: Shift Requirements (`testEndToEndWorkflow`)
Admin sets default shift requirements for tomorrow:
- Shift 1: Requires 2 employees
- Shift 2: Requires 2 employees

### Step 4: Planning & Assignments
Admin creates a schedule (planning) that assigns wishes to shifts:

**Shift 1 Assignment**: 
- Assigned Wishes: EMP001, EMP004 (2 wishes)
- Requirement Met: ✓ 2 employees assigned

**Shift 2 Assignment**:
- Assigned Wishes: EMP003, EMP006 (2 wishes)
- Requirement Met: ✓ 2 employees assigned

**Result**: Complete schedule created with all employees assigned to their preferred shifts

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/wishbook/
│           ├── models/           # Data models for API requests/responses
│           │   ├── Employee.java
│           │   ├── WishRequest.java
│           │   ├── WishResponse.java
│           │   ├── PlanningRequest.java
│           │   ├── PlanningResponse.java
│           │   ├── ShiftAssignment.java
│           │   ├── ShiftRequirement.java
│           │   ├── ShiftRequirementRequest.java
│           │   ├── ShiftRequirementResponse.java
│           │   └── EmployeeAssignment.java
│           └── utils/             # Helper utilities
│               ├── RestApiClient.java      # Fluent API for HTTP requests
│               ├── TestDataLoader.java     # Load test data from JSON
│               ├── WishHelper.java         # Wish-related operations
│               └── PlanningHelper.java     # Planning-related operations
│
└── test/
    ├── java/
    │   └── com/wishbook/tests/
    │       └── WishCreationTest.java       # Main E2E test class
    └── resources/
        └── testdata/
            ├── employee.json               # Test employee data
            ├── wish_templates.json         # Wish template configurations
            └── testng.xml                  # TestNG suite configuration
```

## Setup & Configuration

### Prerequisites
- Java 11+
- Maven 3.8.9+
- Active Wishbook API server running on `http://localhost:3000`

### Installation

1. Clone the repository
```bash
git clone <repository-url>
cd wishbook-api-automation
```

2. Build the project
```bash
mvn clean install
```

3. Configure API endpoint (optional)
```bash
# Default: http://localhost:3000
# Override with:
mvn test -Dbase.url=http://your-api-url:port
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run with specific API URL
```bash
mvn test -Dbase.url=http://localhost:3000
```

### Run specific test class
```bash
mvn test -Dtest=WishCreationTest
```

### View test reports
```bash
open target/surefire-reports/Wishbook\ API\ Test\ Suite/Wish\ Creation\ Tests.html
```

## Test Data

### Test Employees (`employee.json`)
- **EMP001** - John Doe (EMPLOYEE)
- **EMP003** - Mike Johnson (EMPLOYEE)
- **EMP004** - Sarah Williams (EMPLOYEE)
- **EMP005** - Robert Brown (ADMIN)
- **EMP006** - Serena Williams (EMPLOYEE)

### Wish Templates (`wish_templates.json`)
Pre-configured wish templates with dynamic dates:
- Wish for Today (shifts 1 & 2)
- Wish for Tomorrow (shifts 1 & 2)
- Wish for Day+2 (shift 1)
- Wish for Day+3 (shifts 1 & 2)

## API Endpoints Used

| Operation | Endpoint | Method | Purpose |
|-----------|----------|--------|---------|
| Generate Token | `/auth/generate-token` | POST | Get auth token for employee |
| Create Wish | `/wishes` | PUT | Employee creates shift wish |
| Set Shift Requirements | `/shift-requirements/{date}` | POST | Admin sets shift staffing needs |
| Create Planning | `/planning/assign` | POST | Admin creates schedule assignments |

## Key Features

### RestAssured Fluent API
```java
RestApiClient.request()
    .withToken(token)
    .withBody(request)
    .postAndGet(endpoint, ResponseClass.class);
```

### Dynamic Date Handling
- Tests use dynamic dates relative to current date
- Dates are calculated using `PlanningHelper.getDateWithOffset()`
- Avoids hardcoded dates that become outdated

### Global Storage
- Employee tokens stored in `employeeTokens` map
- Wishes stored in `wishesByEmployee` map
- Allows sharing data between test methods

### Error Handling
- Automatic deserialization with Jackson
- `@JsonInclude` ignores null fields
- Proper exception handling for API errors

## Test Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    SETUP (@BeforeClass)                     │
│  - Initialize dynamic dates (today, +1, +2, +3)             │
│  - Load employees from employee.json                        │
│  - Create wish templates with dynamic dates                 │
│  - Generate auth tokens for all employees                   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│        TEST 1: testCreateWishesForAllEmployees              │
│  - 4 employees create 1 shift wish each for tomorrow        │
│  - Wishes stored in wishesByEmployee map                    │
│  - Validates: wishId is not null                            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│        TEST 2: testEndToEndWorkflow                         │
│  (dependsOnMethods: testCreateWishesForAllEmployees)        │
│                                                             │
│  1. Set Shift Requirements (Admin)                          │
│     - Shift 1: 2 employees needed                           │
│     - Shift 2: 2 employees needed                           │
│                                                             │
│  2. Create Planning/Assignments (Admin)                     │
│     - Shift 1: Assign EMP001, EMP004 wishes                │
│     - Shift 2: Assign EMP003, EMP006 wishes                │
│                                                             │
│  3. Validate:                                               │
│     - Planning ID is generated                             │
│     - Assignments created for both shifts                  │
└─────────────────────────────────────────────────────────────┘
```

## Dependencies

- **RestAssured** 5.3.2 - REST API testing
- **TestNG** 7.8.0 - Test framework
- **Jackson** 2.15.3 - JSON serialization/deserialization
- **SLF4J** 2.0.9 - Logging
- **Groovy** 4.0.15 - RestAssured DSL support

## Troubleshooting

### "Schedule is locked" error
- The test tries to create wishes/planning for future dates
- Today's schedule is typically locked for modifications
- Solution: Tests use `todayPlus1` to avoid locked dates

### "Cannot deserialize" errors
- Missing fields in model classes
- Solution: Check API response and add corresponding @JsonProperty fields

### Connection refused
- API server not running
- Solution: Start Wishbook API on `http://localhost:3000`

## Extensibility

The framework is designed to be easily extended:

1. **Add new test methods**: Inherit from `WishCreationTest` or create new test class
2. **Add new API endpoints**: Extend `RestApiClient` with new methods
3. **Add new helpers**: Create new utility classes in `com.wishbook.utils`
4. **Update test data**: Modify JSON files in `testdata/` directory

## Best Practices Implemented

✓ Page Object Model pattern (via Helper classes)
✓ Data-driven testing (JSON test data)
✓ Test dependency management (dependsOnMethods)
✓ Fluent API for readable assertions
✓ Proper error handling and logging
✓ Dynamic test data (date offset calculations)
✓ Separation of concerns (models, utils, tests)
✓ Minimal test code duplication

## Contributing

When adding new tests:
1. Use meaningful test names
2. Add comments explaining complex logic
3. Follow the existing code style
4. Ensure tests are independent where possible
5. Update test data files if needed
6. Run full test suite before committing

## License

This project is part of the Wishbook API test automation suite.

## Contact

For issues or questions, please refer to the project documentation or contact the development team.
