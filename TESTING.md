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

## 5. Equivalence Class Testing

Equivalence class testing partitions inputs into groups where all values behave similarly. Each group is represented by one test case.

**Employee ID Classes:**
- **Valid IDs:** "E001", "E002", etc. - System accepts and creates employee. Tested in `shouldAddEmployeeSuccessfully()`
- **Duplicate IDs:** "E001" when already exists - System rejects with exception. Tested in `shouldThrowExceptionForDuplicateEmployeeId()`
- **Empty IDs:** "" - System rejects with exception. Tested in `shouldThrowExceptionWhenEmployeeIdIsBlank()`

**Login Credentials Classes:**
- **Valid credentials:** Username exists AND password matches - Login succeeds. Tested in `shouldLoginSuccessfully()`
- **Valid username, wrong password:** "john" with "wrongpass" - Login fails with InvalidPasswordException. Tested in `shouldThrowExceptionForInvalidPassword()`
- **Non-existent user:** "ghost" - Login fails with UserNotFoundException. Tested in `shouldThrowExceptionWhenUserNotFound()`

**Leave Request Classes:**
- **Valid leave:** Days requested ≤ available balance (e.g., 3 days when 10 available) - Leave approved. Tested in `shouldApplyLeaveSuccessfully()`
- **Exceeds balance:** Days requested > available balance (e.g., 20 days when 10 available) - Exception thrown. Tested in `shouldThrowExceptionWhenLeaveExceedsBalance()`
- **Duplicate request:** Same leave ID applied twice - Exception thrown. Tested in `shouldThrowExceptionForDuplicateLeaveRequestId()`

**String Field Classes:**
- **Valid strings:** Non-empty names, emails, phone numbers - Accepted. Tested in `shouldCreateEmployeeSuccessfully()`
- **Empty/blank strings:** "", " " for name, email, phone - Rejected with exception. Tested in `shouldThrowExceptionWhenFullNameIsBlank()`, `shouldThrowExceptionWhenEmailIsBlank()`, `shouldThrowExceptionWhenPhoneIsBlank()`

**File:** `EmployeeTest.java`, `AuthServiceTest.java`, `LeaveServiceTest.java`

---

## 6. Decision Table Testing

Decision table testing combines multiple conditions to determine expected outcomes.

**User Login Decision Logic:**
When a user attempts to login with username and password, the system checks:
1. Does the username exist in the database?
2. Is the password correct for that user?

- **Case 1:** Username exists AND password correct → Login succeeds with user details. Tested in `shouldLoginSuccessfully()`
- **Case 2:** Username exists AND password incorrect → Invalid password exception thrown. Tested in `shouldThrowExceptionForInvalidPassword()`
- **Case 3:** Username does not exist → User not found exception thrown. Tested in `shouldThrowExceptionWhenUserNotFound()`

**Employee Creation Decision Logic:**
When creating a new employee, the system validates:
1. Is the employee ID unique (not already in database)?
2. Is the name field non-empty?
3. Is the email field non-empty?
4. Is a valid department assigned?

- **Case 1 (All Valid):** ID unique, name provided, email provided, department valid → Employee created successfully. Tested in `shouldAddEmployeeSuccessfully()`
- **Case 2 (Duplicate ID):** ID already exists (regardless of other fields) → Duplicate exception thrown. Tested in `shouldThrowExceptionForDuplicateEmployeeId()`
- **Case 3 (Empty Name):** ID unique but name is empty → Name validation exception thrown. Tested in `shouldThrowExceptionWhenFullNameIsBlank()`
- **Case 4 (Empty Email):** ID unique, name valid, but email is empty → Email validation exception thrown. Tested in `shouldThrowExceptionWhenEmailIsBlank()`
- **Case 5 (No Department):** Other fields valid but department is null → Department validation exception thrown. Tested in `shouldThrowExceptionWhenDepartmentIsNull()`

**Leave Approval Decision Logic:**
When manager approves leave:
1. Does the leave request exist in database?
2. Is the leave status currently PENDING?
3. Is the approver a valid manager?

- **Case 1 (Valid Approval):** Leave exists, status is PENDING, approver is manager → Leave approval succeeds, status changes to APPROVED. Tested in `shouldApproveLeaveSuccessfully()`
- **Case 2 (Invalid State):** Leave already APPROVED → System blocks re-approval (implicit validation)
- **Case 3 (Not Found):** Leave ID does not exist → Leave not found exception (implicit validation)

**Files:** `AuthServiceTest.java`, `EmployeeTest.java`, `EmployeeServiceTest.java`, `LeaveServiceTest.java`

---

## 7. State Transition Testing

**Leave Request State Machine:**

A leave request starts in the **PENDING** state and can transition to three possible terminal states:
- **APPROVED** (✅) - Manager approves the leave request
- **REJECTED** (❌) - Manager rejects the leave request  
- **CANCELLED** (⏹️) - Employee cancels their own leave request

**Valid State Transitions:**

- **PENDING → APPROVED:** When a manager approves a pending leave request, the system updates the status to APPROVED and deducts the leave from employee's balance. Tested in `shouldApproveLeaveSuccessfully()`
- **PENDING → REJECTED:** When a manager rejects a pending leave request, the system updates the status to REJECTED and does not deduct any leave. Tested in `shouldRejectLeaveSuccessfully()`
- **PENDING → CANCELLED:** When an employee cancels their own pending leave request before manager decision, the system updates the status to CANCELLED. Tested in `shouldCancelLeaveSuccessfully()`

**Invalid State Transitions (System Prevents These):**

- **APPROVED → PENDING:** Cannot revert an approved leave back to pending - system blocks this
- **REJECTED → APPROVED:** Cannot re-approve a rejected leave - system blocks this
- **CANCELLED → PENDING:** Cannot restore a cancelled leave - system blocks this

These invalid transitions are prevented by validation logic in the `LeaveRequest` model class, which only allows transitions from the PENDING state to terminal states (APPROVED, REJECTED, or CANCELLED).

**File:** `LeaveServiceTest.java`, `LeaveRequest.java`

---

## 8. Use Case Testing

**Primary Use Case: Employee Leave Request Workflow**

**Actors:** Employee, Manager

**Preconditions:**
- System is initialized with database
- Department exists (IT Department)
- Employee and Manager exist in system with proper roles
- Both users are registered and authenticated

**Main Flow:**
1. Employee logs into system with valid credentials
2. Employee views their current leave balance (10 days available)
3. Employee submits leave request for 3 days (VACATION type, April 10-12, 2026)
4. System validates: leave type valid, dates valid, available balance sufficient
5. Leave request created with status PENDING and saved to database
6. Manager receives leave request for review
7. Manager approves the leave request after reviewing
8. System updates leave request status to APPROVED
9. System deducts 3 days from employee's leave balance (10 → 7)
10. Approval details recorded (approver ID, timestamp)

**Postconditions:**
- Leave request status: APPROVED
- Employee leave balance: 7 days (reduced from 10)
- Department still has both Employee and Manager
- All changes persisted to database

**Test File:** `HRSystemTest.java` - `shouldCompleteEmployeeLeaveWorkflow()`

This use case validates the complete integration of multiple system components working together to process a leave request from submission to approval.

---