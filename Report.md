# ENSE 375 – Software Testing and Validation






## Human Resource Management System (HRMS)



### Team members
- Yash Sanjaybhai Jadav (200519386)
- Ishankumar Vimalkumar Patel (200499369)
- Dur e Sameen Bukhari (200542362)  




 
Table of Contents
1	Introduction	5
2	Design Problem	6
2.1	Problem Definition	6
2.2	Design Requirements	6
2.2.1	Functions	6
2.2.2	Objectives	6
2.2.3	Constraints	6
3	Solution	7
3.1	Solution 1	7
3.2	Solution 2	7
3.3	Final Solution	7
3.3.1	Components	7
3.3.2	Environmental, Societal, Safety, and Economic Considerations	7
3.3.3	Test Cases and Results	7
3.3.4	Limitations	7
4	Team Work	8
4.1	Meeting 1	8
4.2	Meeting 2	8
4.3	Meeting 3	8
4.4	Meeting 4	8
5	Project Management	9
6	Conclusion and Future Work	10
7	References	11
8	Appendix	12

•	Proof read the text for typing and grammar mistakes.
•	Follow the IEEE Bibliography style for the references by selecting "References/ Citations & Bibliography/ Style". 
List of Figures
 
List of Tables            
     
1	Introduction
Human Resource Management Systems (HRMS) represent a critical category of enterprise software designed to automate and streamline the administrative and strategic functions of human resource departments. These systems consolidate employee information, manage payroll processing, track attendance and leave, facilitate performance evaluations, and ensure compliance with labor regulations. In an era where data-driven decision-making and operational efficiency are paramount, HRMS solutions enable organizations to transition from manual, error-prone processes to integrated, reliable digital workflows.
This project involves the development of a Human Resource Management System as part of ENSE 375 – Software Testing and Validation. The primary objective is not only to create a functional software application but also to rigorously apply and demonstrate software testing methodologies within a realistic development context. The system will be built using the Java programming language, organized according to the Model-View-Controller (MVC) architectural pattern, and developed following a test-driven development (TDD) approach. A comprehensive suite of tests will be implemented, encompassing unit, integration, path, data flow, boundary value, equivalence class, decision table, state transition, and use case testing techniques.
The selection of an HRMS as the project focus is deliberate. Human resource processes involve complex business rules, sensitive personal and financial data, and workflows that impact organizational fairness and legal compliance. This makes the domain exceptionally well-suited for illustrating the importance of systematic validation. Errors in payroll calculations, leave accruals, or access controls can lead to significant financial loss, legal repercussions, and erosion of employee trust. Therefore, building and thoroughly testing an HRMS provides a meaningful context for exploring the practical challenges and solutions in software quality assurance.
This report documents the entire engineering design process undertaken for the project. It begins by defining the problem and specifying requirements, proceeds through iterative design and solution evaluation, and concludes with the implementation of a tested prototype. The associated GitHub repository contains all source code, test suites, and additional documentation, including a TESTING.md file that details the test strategy and results. The following sections provide a complete account of the project's conception, design, and execution, serving as both a technical record and a demonstration of software engineering and validation principles applied in practice.                 

        
2	Design Problem
2.1	Problem Definition
In modern organizational environments, particularly within small and medium-sized enterprises, human resource management is often handled through a collection of loosely connected tools such as spreadsheets, paper-based records, email communication, and basic digital applications. Core HR activities—including employee record management, attendance tracking, leave approvals, payroll calculations, and performance monitoring—are frequently managed in isolation rather than through an integrated system. This fragmented approach results in inefficiencies, inconsistencies in data, and an increased likelihood of operational errors.
From a software engineering and validation perspective, such environments present a significant challenge. Disconnected workflows and manual processes make it difficult to systematically verify correctness, ensure data integrity, and validate system behavior under varying operational scenarios. The lack of a unified structure complicates software testing activities such as tracing data flow, validating edge cases, and ensuring consistent behavior across interdependent components.
Several challenges arise from the existing approaches to human resource management. Employee data stored across multiple sources creates inconsistencies and increases the difficulty of maintaining a single, reliable version of information. Administrative staff are burdened with repetitive manual tasks, reducing productivity and increasing dependence on error-prone human intervention. Calculations related to payroll, leave balances, and deductions are particularly sensitive to defects, as even small errors can have significant financial and organizational consequences. Furthermore, limited transparency and restricted employee access to personal records reduce usability and complicate validation of system workflows. The handling of sensitive personal and financial data also introduces concerns related to privacy, access control, and compliance with accepted data protection practices.
The impact of these challenges extends beyond operational inefficiency. Employees, managers, and administrators rely on accurate and timely information to make decisions, and software failures in HR systems can directly affect trust, fairness, and organizational credibility. As such, the problem is not only functional in nature but also demands careful consideration of reliability, security, ethical handling of data, and societal expectations related to transparency and accountability.
The objective of this project is to address these challenges by designing, developing, and thoroughly testing a Human Resource Management System (HRMS) that consolidates essential HR functions into a single, cohesive software application. The system is intended to reduce manual effort, improve data consistency, and support reliable decision-making while serving as a realistic platform for applying software testing and validation principles emphasized in ENSE 375.
A key focus of the project is the design and execution of comprehensive and systematic test suites. The HRMS is deliberately structured to support rigorous verification and validation using multiple testing techniques, including unit, integration, path, data flow, boundary value, equivalence class, decision table, state transition, and use case testing. This ensures that both normal and exceptional system behaviors can be objectively evaluated.
The scope of the project is intentionally defined to be small to medium in size, making it suitable for an approximate two-month development and testing timeline. At the same time, the system includes sufficient functional complexity and inter-module interaction to meaningfully demonstrate software testing challenges and solutions. The problem definition is shaped by realistic considerations such as secure handling of sensitive data, ethical treatment of employee information, reliability of critical calculations, societal impact of system correctness, and practical constraints on development and testing resources.	
Based on the problem context, challenges, and testing considerations outlined above, the following section defines the formal design requirements of the system, including its functions, objectives, and constraints.

2.2	Design Requirements
This section has the following three subsections:
2.2.1	Functions
•	Provide functions of the design project. Remember that the functions contain verbs.
2.2.2	Objectives
•	Provide objectives of the design project. Remember that the objectives are specified as adjectives.
2.2.3	Constraints
•	Provide constraints here. Remember that the constraints are binary (either satisfied or not).

 
3	Solution
In this section, you will provide an account of some solutions your team brainstormed to implement and test the project. Some solutions might not have all the desired features, some might not satisfy the constraints, or both. These solutions come up in your mind while you brainstorm ways of implementing all the features while meeting the constraints. Towards, the end you select a solution that you think has all the features, testable and satisfies all the constraints. Remember that an engineering design is iterative in nature! 
3.1	Solution 1
Write a brief description of your first solution and provide the reasons in terms of testing for not selecting this one. 
3.2	Solution 2
This is an improved solution but might not be the final solution that you select. Give a brief description of this solution here. Again focus on its testing attributes. 
3.3	Final Solution
This is the final solution.  Explain why it is better than other solutions (focus more on testing). You may use a table for comparison purposes. After providing the reason for selecting this solution, detail it below.
3.3.1	Components
What components you used in the solution? What is the main purpose of using individual component? What testing method did you employ for each component? Provide a block diagram (with a numbered caption, such as Fig. 1) representing the connectivity and interaction between all the components.
3.3.2	Environmental, Societal, Safety, and Economic Considerations
Explain how your engineering design took into account environmental, societal, economic and other constraints into consideration. It may include how your design has positive contributions to the environment and society? What type of economic decisions you made? How did you make sure that the design is reliable and safe to use? 
3.3.3	Test Cases and results
What test suits did you design to test your prototype? How did you execute the test cases to test the prototype?
3.3.4	Limitations
Every product has some limitations, and so is the case with your design product. Highlight some of the limitations of your solution here. 




4	Team Work
Since this is a group project, you must have a fair distribution of tasks among yourselves. To this end, you must hold meetings to discuss the distribution of tasks and to keep a track of the project progress.
4.1	Meeting 1
Time: Month Date, Year, hour: minutes am/pm to hour: minutes am/pm
Agenda: Distribution of Project Tasks
Team Member	Previous Task	Completion State	Next Task
Team member 1	N/A	N/A	Task 1
Team member 2	N/A	N/A	Task 2
Team member 3	N/A	N/A	Task 3

4.2	Meeting 2
Time: Month Date, Year, hour: minutes am/pm to hour: minutes am/pm
Agenda: Review of Individual Progress
Team Member	Previous Task	Completion State	Next Task
Team member 1	Task 1	80%	Task 1, Task 5
Team member 2	Task 2	50%	Task 2
Team member 3	Task 3	100%	Task 6

4.3	Meeting 3
Provide a similar description here.
4.4	Meeting 4
Provide a similar description here. 
 
5	Project Management
Provide a Gantt chart showing the progress of your work here. Mention all the tasks along with their predecessors. Provide the slack time of each task and identify the critical path. 
6	Conclusion and Future Work
•	A summary of what you achieved. Mention all the design functions and objectives that you achieved while satisfying testing requirements?
•	While keeping the limitations of your solution, provide recommendations for future design improvements.
 
7	References

•	Use the IEEE reference style.
•	Do not put any reference if it is not cited in the text. 
 
8	Appendix
If you want to provide an additional information, use this appendix.

<img width="471" height="597" alt="image" src="https://github.com/user-attachments/assets/4ebeac14-a337-429c-a591-f71071828751" />
