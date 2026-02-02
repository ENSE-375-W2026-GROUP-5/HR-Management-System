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
