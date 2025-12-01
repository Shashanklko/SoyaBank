
## **SoyaBank Overview**

**SoyaBank** is a **console-based banking management system** built using **Spring Boot** and **Java 21**. It provides essential banking functionalities for both **customers** and **administrators**, leveraging a **MySQL database** for persistent storage via **JDBC**.

---

### **Key Features**

**Customer Features:**

* **Account Management**: Create accounts, login, view personal account details.
* **Banking Operations**:

  * Deposit money
  * Withdraw money
  * Transfer funds between accounts
* **Transaction History**: View all past transactions for accountability.

**Admin Features:**

* **Admin Authentication**: Secure login for administrators.
* **Customer Management**: View all customer details.
* **Bank Statistics**: Monitor transactions and bank-wide statistics.

---

### **Technology Stack**

| Component             | Technology        |
| --------------------- | ----------------- |
| Framework             | Spring Boot 3.2.0 |
| Language              | Java 21           |
| Database              | MySQL             |
| Database Connectivity | JDBC              |
| Build Tool            | Maven             |

---

### **Project Structure**

```
src/main/java/com/SoyaBank/
├── Bank.java                  # Main application entry point
├── ConnectionDB.java          # Handles DB connections
└── Banking_Management/
    ├── CreateAccount.java     # Service to create new customer accounts
    ├── LoginAccount.java      # Customer login service
    ├── AdminLogin.java        # Admin authentication
    ├── AdminPanel.java        # Admin operations (manage/view customers)
    ├── UpdateBalance.java     # Deposit/Withdraw operations
    ├── TransferMoney.java     # Money transfer between accounts
    ├── GetBalance.java        # Balance inquiry service
    ├── ViewAccountDetails.java# View customer account details
    ├── DepositMoney.java      # Alternative deposit service
    └── TransactionHistory.java# Logs and retrieves transaction history
```

---

### **Summary**

SoyaBank is a **fully functional console banking system** that separates **customer services** and **admin management**, uses **Spring Boot** for structure, **MySQL** for persistent storage, and **JDBC** for database interactions. Its modular design allows each banking operation to be handled by a dedicated service class, ensuring maintainability and scalability.

