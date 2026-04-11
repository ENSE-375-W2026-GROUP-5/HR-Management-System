### This is the code folder for HRMS

---

The app uses maven and SQLite dependency so users do not need to install SQLite separately

---


### To run the app,

1. Make sure maven is installed
2. cd to project root directory
   ```
   cd Codebase/hrms
   ```
3. Execute
   ```
   mvn exec:java -Dexec.mainClass="hrms.app.Main"
   ```

Use the following credentials for login 

| Role     | Username | Password   |
|----------|----------|------------|
| ADMIN    | Ishan    | admin123   |
| MANAGER  | John     | manager123 |
| EMPLOYEE | Anny     | emp123     |

---

### To run the tests
1. cd to project root directory
   ```
   cd Codebase/hrms
   ```
2. Execute
   ```
   mvn clean test
   ```
