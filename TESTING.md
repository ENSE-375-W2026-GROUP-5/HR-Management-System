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

**Partitioning inputs into valid and invalid equivalence classes:**

### 5.1 Employee ID Equivalence Classes
| Class | Example | Status | Test |
|-------|---------|--------|------|
| Valid IDs | "E001", "E002" | ✅ Accept | `shouldAddEmployeeSuccessfully()` |
| Duplicate IDs | "E001" (already exists) | ❌ Reject | `shouldThrowExceptionForDuplicateEmployeeId()` |
| Empty IDs | "" | ❌ Reject | `shouldThrowExceptionWhenEmployeeIdIsBlank()` |

### 5.2 Login Credentials Equivalence Classes
| Class | Username | Password | Expected Result | Test |
|-------|----------|----------|-----------------|------|
| Valid | "john" | "pass123" | ✅ Login Success | `shouldLoginSuccessfully()` |
| Invalid Password | "john" | "wrongpass" | ❌ Exception | `shouldThrowExceptionForInvalidPassword()` |
| Non-existent User | "ghost" | "pass123" | ❌ Exception | `shouldThrowExceptionWhenUserNotFound()` |

### 5.3 Leave Request Equivalence Classes
| Class | Days Requested | Available Balance | Expected Result | Test |
|-------|----------------|--------------------|-----------------|------|
| Valid | 3 | 10 | ✅ Approved | `shouldApplyLeaveSuccessfully()` |
| Exceeds Balance | 20 | 10 | ❌ Exception | `shouldThrowExceptionWhenLeaveExceedsBalance()` |
| Duplicate Request | "L001" | Valid | ❌ Exception | `shouldThrowExceptionForDuplicateLeaveRequestId()` |

### 5.4 Email Field Equivalence Classes
| Class | Example | Status |
|-------|---------|--------|
| Valid Format | "john@test.com" | ✅ Accept |
| Empty String | "" | ❌ Reject |
| File: `EmployeeTest.java` - `shouldThrowExceptionWhenEmailIsBlank()` |

---

## 6. Decision Table Testing

### 6.1 User Login Decision Table

**Function:** `AuthService.login(username, password)`

| Test Case | Username Exists | Password Correct | Role Valid | Expected Result | Test File |
|-----------|-----------------|------------------|-----------|-----------------|-----------|
| 1 | Yes | Yes | Yes | ✅ Login Success | `shouldLoginSuccessfully()` |
| 2 | Yes | No | Yes | ❌ InvalidPasswordException | `shouldThrowExceptionForInvalidPassword()` |
| 3 | No | - | - | ❌ UserNotFoundException | `shouldThrowExceptionWhenUserNotFound()` |

**File:** `AuthServiceTest.java`

### 6.2 Employee Creation Decision Table

**Function:** `EmployeeService.addEmployee(...)`

| Test Case | ID Exists | Name Valid | Email Valid | Department Valid | Status | Test |
|-----------|-----------|-----------|-------------|------------------|--------|------|
| 1 | No | Yes | Yes | Yes | ✅ Success | `shouldAddEmployeeSuccessfully()` |
| 2 | Yes | Yes | Yes | Yes | ❌ DuplicateException | `shouldThrowExceptionForDuplicateEmployeeId()` |
| 3 | No | No | Yes | Yes | ❌ InvalidName | `shouldThrowExceptionWhenFullNameIsBlank()` |
| 4 | No | Yes | No | Yes | ❌ InvalidEmail | `shouldThrowExceptionWhenEmailIsBlank()` |
| 5 | No | Yes | Yes | No | ❌ InvalidDept | `shouldThrowExceptionWhenDepartmentIsNull()` |

**File:** `EmployeeTest.java`, `EmployeeServiceTest.java`

### 6.3 Leave Approval Decision Table

**Function:** `LeaveService.approveLeave(leaveId, approverId)`

| Test Case | Leave Exists | Leave Status | Approver Valid | Expected Result | Test |
|-----------|--------------|--------------|----------------|-----------------|------|
| 1 | Yes | PENDING | Yes | ✅ Approved | `shouldApproveLeaveSuccessfully()` |
| 2 | Yes | APPROVED | Yes | ❌ InvalidState | (Implicit validation) |
| 3 | No | - | Yes | ❌ NotFound | (Implicit validation) |

**File:** `LeaveServiceTest.java`

---

## 7. State Transition Testing

**Leave Request State Machine:**
              ┌─────────────┐
              │  PENDING    │
              └──────┬──────┘
                /    |    \
               /     |     \
              /      |      \
     APPROVED    REJECTED   CANCELLED
       (✅)       (❌)         (⏹️)

### 7.1 Valid State Transitions

| Transition | From State | To State | Trigger | Test Case |
|-----------|-----------|----------|---------|-----------|
| 1 | PENDING | APPROVED | Manager approves leave | `shouldApproveLeaveSuccessfully()` |
| 2 | PENDING | REJECTED | Manager rejects leave | `shouldRejectLeaveSuccessfully()` |
| 3 | PENDING | CANCELLED | Employee cancels leave | `shouldCancelLeaveSuccessfully()` |

### 7.2 Invalid State Transitions (Prevented by System)

| Transition | From State | To State | Reason | Status |
|-----------|-----------|----------|--------|--------|
| Invalid 1 | APPROVED | PENDING | Cannot reverse approval | ❌ Blocked |
| Invalid 2 | REJECTED | APPROVED | Cannot re-approve rejected | ❌ Blocked |
| Invalid 3 | CANCELLED | PENDING | Cannot restore cancelled | ❌ Blocked |

**File:** `LeaveServiceTest.java`
**Model:** `LeaveRequest.java` validates state transitions

**Test Examples:**
- `shouldApproveLeaveSuccessfully()` - Tests PENDING → APPROVED
- `shouldRejectLeaveSuccessfully()` - Tests PENDING → REJECTED
- `shouldCancelLeaveSuccessfully()` - Tests PENDING → CANCELLED

---

