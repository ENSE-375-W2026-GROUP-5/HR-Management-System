# TESTING.md - HRMS Test Plan

## 1. Path Testing

**Selected Function:** `EmployeeService.addEmployee()`

**Execution Paths Tested:**

1. **Happy Path (Valid Input)**
   - File: `EmployeeServiceTest.java`
   - Test: `shouldAddEmployeeSuccessfully()`
   - Flow: Valid parameters → Employee created successfully → Returns Employee object
   
2. **Error Path (Duplicate Employee ID)**
   - File: `EmployeeServiceTest.java`
   - Test: `shouldThrowExceptionForDuplicateEmployeeId()`
   - Flow: Duplicate ID provided → IllegalArgumentException thrown → Transaction rolled back

3. **Data Flow Through Function:**
   - Input: Employee ID, Name, Email, Phone, Department, Role, Designation, Salary, Leave Balance, Status, Manager ID
   - Processing: Validation → Database insertion → Object creation
   - Output: Employee object with persisted data

---

## 2. Data Flow Testing

**Selected Function:** `AuthService.login()`

**Data Flow Analysis:**

| Stage | Input | Processing | Output |
|-------|-------|-----------|--------|
| 1. Input | Username: "john", Password: "pass123" | Received from user |  |
| 2. Validation | Username exists in database | Query UserRepository | Found or Not Found |
| 3. Transformation | Password comparison | Validate plain text password | Match or Mismatch |
| 4. Decision | Credentials valid? | Branch: Yes/No | |
| 5. Output (Valid) | Matching credentials | Retrieve UserAccount object | Return UserAccount with Role, LinkedEmployeeId |
| 6. Output (Invalid) | Invalid password | Throw IllegalArgumentException | Error message |

**Test Cases:**
- File: `AuthServiceTest.java`
- `shouldLoginSuccessfully()` - Valid data flow path
- `shouldThrowExceptionForInvalidPassword()` - Invalid password data path
- `shouldThrowExceptionWhenUserNotFound()` - User not found data path

---

## 3. Integration Testing

**Tested Components:**
- DepartmentService + DepartmentRepository
- EmployeeService + EmployeeRepository
- AuthService + UserRepository
- LeaveService + LeaveRepository
- SQLite Database

**Integration Test File:** `HRSystemTest.java`

**Test Case: `shouldCompleteEmployeeLeaveWorkflow()`**

**Workflow Tested:**
Department Creation (IT Department)
->
Employee Creation (Manager E001, Employee E002)
->
User Registration (Both users registered with roles)
->
Leave Application (Employee applies for 3-day vacation leave)
->
Leave Approval (Manager approves the leave request)
->
Validation (Leave status changed to APPROVED, leave balance updated 10→7)

**Components Interaction:**
- Service layer communicates with Repository layer
- Repository layer persists/retrieves data from SQLite database
- Data consistency maintained across multiple operations
- Transactions rollback on errors

**Result:** All components work correctly together with proper data persistence

---

## 4. Boundary Value Testing

**Tested Boundaries in HRMS Application:**

### 4.1 Salary Boundaries
- **Lower Boundary:** Salary = 0 (Invalid)
- **Upper Boundary:** Salary = 1,000,000 (Valid)
- **Negative:** Salary = -5000 (Invalid)
- File: `EmployeeTest.java`

### 4.2 Leave Balance Boundaries
- **Lower Boundary:** Leave Balance = 0 (Valid but no leaves available)
- **Upper Boundary:** Leave Balance = 30 (Valid maximum)
- **Test Case:** `shouldThrowExceptionWhenLeaveExceedsBalance()`
  - Employee has 10 leaves, requests 20 → Exception thrown
  - Employee has 10 leaves, requests 10 → Approved
  - Employee has 10 leaves, requests 11 → Exception thrown

### 4.3 String Field Boundaries
- **Empty String:** "" (Invalid for name, email, phone, designation)
- **Blank String:** " " (Trimmed and validated)
- **File:** `EmployeeTest.java`
- **Tests:**
  - `shouldThrowExceptionWhenFullNameIsBlank()`
  - `shouldThrowExceptionWhenEmailIsBlank()`
  - `shouldThrowExceptionWhenPhoneIsBlank()`

### 4.4 Date Boundaries
- **Valid:** StartDate ≤ EndDate
- **Invalid:** StartDate > EndDate (Not tested but validated in service)

---