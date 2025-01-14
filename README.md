# Banking Application

## Overview
This is a plain Java application designed to manage bank accounts with features like creating accounts, performing deposits & withdrawals, displaying balance, and tracking account operations.

---

## Prerequisites

### Tools and Libraries
- **JDK**: Version 17 or higher.
- **Maven**: For dependency management and building the project.

---

## Setup Instructions

### Clone the Repository
```bash
git https://github.com/azizgouidercap/banking-app-sg.git
cd banking-app-sg
```

### Build the Project
```bash
mvn clean package
```

### Run the Application

#### If Using a Fat JAR:
```bash
java -jar target/bankingapp-1.0-SNAPSHOT.jar
```

---

## Usage

### Create an Account
1. Run the application.
2. Follow the on-screen prompts to create an account by entering the owner's name and account type.

### Perform a Deposit
1. Select the deposit option.
2. Provide the account ID and amount.
3. The application validates and processes the transaction based on account rules.

### Perform a Withdrawal
1. Select the withdrawal option.
2. Provide the account ID and amount.
3. The application validates and processes the transaction based on account rules.

### Display Balance
1. Select the balance inquiry option.
2. Provide the account ID.
3. The application retrieves and displays the current balance for the account.

### View Account Operations
1. Select the account operations option.
2. Provide the account ID.
3. The application lists all transactions and operations performed on the account.

---

## Technologies Used
- **Java**: Core programming language.
- **Maven**: Dependency and build management.

---

## Contact
For questions or support, please contact:
- **Name**: Aziz GOUIDER
- **Email**: aziz.gouider@capgemini.com
- **GitHub**: [My GitHub Profile](https://github.com/azizgouidercap)
