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