![UofR](assets/logo.png)





# **ENSE 375 – Software Testing and Validation**
---
## Human Resource Management System (HRMS)
---
### Team members
- *Yash Sanjaybhai Jadav (200519386)*
- *Ishankumar Vimalkumar Patel (200499369)*
- *Dur e Sameen Bukhari (200542362)*
---
### 1. Introduction
This project implements a small to medium scale HR management system to implement core human resource functions and empli=oyee record management. The system will be built using a Model View Controller architecture and a test driven development process. The main goal of the the project is to design and execute testing suits using multiple testing techniques like boundary value analysis, equivalence class testing, decision table testing, state transition testing and use case testing. Together these techniques ensure that both normal and edge case behaviours are validated.

---

### 2. Design Problem
#### 2.1 Problem Definition
Small organizations manage HR tasks using spreadsheets or informal processes. This causes difficulty in managing in enforcing policies and track employee data. Due to this, there are high chances of errors in the data. Therefore, to overcome this, an HR management system is required to manage employee profiles to ensure reliable workflow. 
#### 2.2 Design Requirements
##### 2.2.1 Functions 
- Store and update employee profiles
- Authorize access based on roles/hierarchy 
- Generate reports
- Payroll monitoring 
##### 2.2.2 Objectives
- Reliable: Enforces HR policies
- Maintainable: MVC architecture
- Usable: Clean and understandable UI 
- Testable: Allow all components to be tested efficiently
##### 2.2.3 Constraints
- Security: Only accessible to authorized personnel
- Ethics: Display only required information (eg. hide SIN number)
- Economic: Using free/open-source tools and software
- Technical: Simple storage mechanism rather than a large DBMS
- Time: Deliver an MVP within the given time frame
### 3. Solution
#### 3.1 Solution 1
The initial solution was to make an HRMS which only allowed minimalistic functions like storing employee information and salary. It lacked several critical features required for a complete HR system. For example, it did not support leave management, role-based access control, or proper data validation.
From a testing perspective, this solution was limited because:
- it did not provide enough grounds for testing
- it did not acheive project requirements due to its low complexity.
#### 3.2 Solution 2
The next improvised solution was to include all the feature for a classic human resource management system. This included leave management, Role based access control and new employee creation. The only drawback of this solution was that it used hashmaps to store employee data. Therefore, data would be gone after the app was closed. 
#### 3.3 Final Solution
The final design is a fully structured MVC based system with database persistence. This solution used SQLite to store and retrieve data, meaning that even if the app was shutdown, data was accessible. 
This solution is better than the rest because it has a wider scope for testing and code coverage.
##### 3.3.1 Components
| Component         | Purpose                                                  | Testing Method    |
| ----------------- | -------------------------------------------------------- | ----------------- |
| Model             | Represents entities (Employee, Department, LeaveRequest) | Unit tests        |
| Repository        | Handles database operations                              | Integration tests |
| Service           | Business logic                                           | Unit tests        |
| Controller        | Connects UI and services                                 | Controller tests  |
| View (CLI)        | User interaction                                         | Manual testing    |
| Database (SQLite) | Data persistence                                         | Integration tests |

Architecture Flow: UI → Controller → Service → Repository → Database

##### 3.3.2 Environmental, Societal, Safety, and Economic Considerations
- Environmental: Reduces paper-based HR processes
- Societal: Improves fairness in leave approvals via structured rules
- Economic: Uses SQLite (no licensing cost)
- Safety & Reliability: Validation checks prevent invalid data such as negative salary, invalid leave requests etc.

##### 3.3.3 Testing Cases and Results
Multiple testing techniques were used to verify the reliability of the application 

- Unit Testing
Unit testing was used to test individual classes and methods in isolation, especially in the service and controller layers. This helped confirm that each component behaved correctly on its own. For example:

    - Testing employee creation in EmployeeService
    - Testing user registration and login in AuthService
    - Testing leave application, approval, rejection, and cancellation in LeaveService

    Result:
    The unit tests confirmed that the major business logic functions worked as expected for valid and invalid inputs.

- Integration Testing
Integration testing was used to verify that the SQLite database worked together correctly with the rest of the components

    Result:
    The integration test showed that the system components interacted correctly and that data was stored and retrieved properly from the database.

- Equivalence Class Testing
Equivalence class testing was used by dividing inputs into valid and invalid groups, then selecting representative values from each group. FOr example:

    - Valid employee ID vs duplicate employee ID
    - Valid leave request vs leave request exceeding leave balance
    - Valid login credentials vs invalid credentials

    Result:
    This testing confirmed that the system correctly accepted valid input classes and rejected invalid ones.

- Boundary Value Testing
Boundary value testing was used to check values near the limits of valid input ranges. For example:

    - Salary values such as 0 and negative salary
    - Leave balance values such as 0 and negative leave balance
    - Leave request days equal to available leave balance and greater than available leave balance

    Result:
    The system handled boundary conditions correctly in most tested cases, particularly for salary, leave balance, and leave request validation.

- State transition testing 
State transition testing was used for leave request processing because leave requests move through different states.
For example, PENDING → APPROVED, PENDING → REJECTED, PENDING → CANCELLED 

    Result:
    The system correctly allowed valid state changes and blocked invalid transitions.