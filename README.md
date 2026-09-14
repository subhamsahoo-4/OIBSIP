# OIBSIP - Java Development Projects

This repository contains my Java Development internship tasks completed as part of the **Oasis Infobyte Internship Program (OIBSIP)**.

The projects demonstrate Java programming, Object-Oriented Programming (OOP), SOLID principles, application design, input validation, data handling, and database integration.

---

## Projects

### Task 2 - Number Guessing Game

A console-based number guessing game developed using Java.

#### Features

* Generates a random number between 1 and 100
* Allows the user to guess the generated number
* Provides higher/lower hints after each guess
* Supports different difficulty levels
* Tracks the number of attempts
* Maintains round history
* Displays game results and scores
* Allows the player to start another round

#### Technology

* Java
* Object-Oriented Programming
* Collections
* Random number generation

---

### Task 3 - ATM Interface

A console-based ATM simulation developed using Java with an Object-Oriented design.

#### Features

* User ID and PIN authentication
* Fixed-length 4-digit PIN validation
* Maximum 3 incorrect authentication attempts
* Account balance checking
* Cash withdrawal
* Cash deposit
* Account-to-account money transfer
* Insufficient funds validation
* Transaction history using `ArrayList`
* Transfer-in and transfer-out transaction logging
* Logout functionality
* Quit/exit functionality
* Multiple classes following OOP and SOLID principles

#### Technology

* Java
* Object-Oriented Programming
* SOLID principles
* `ArrayList`
* Java Collections
* Console-based application

#### Main Classes

```text
ATM
Account
Bank
BankOperations
Transaction
MenuAction
DepositAction
WithdrawAction
TransferAction
BalanceAction
HistoryAction
LogoutAction
QuitAction
Main
```

---

### Task 5 - Digital Library Management System

A digital library management system developed using Java and Spring Boot.

#### Features

##### Admin

* Admin login
* Add books
* Edit books
* Delete books
* View issued books
* View due dates
* Manage registered members
* Activate/deactivate members
* View fines
* Mark fines as paid
* View member messages and queries

##### User

* User registration
* User login
* Browse the book catalogue
* Search books by title and author
* Issue books
* Return books
* View borrowing information
* Fine management

#### Technology

* Java
* Spring Boot
* MySQL
* JDBC / database integration
* HTML
* CSS
* JavaScript

---

## Repository Structure

```text
OIBSIP/
│
├── ATM_Interface/
│   ├── src/
│   └── README.md
│
├── NumberGuessingGame/
│   ├── src/
│   └── README.md
│
├── Java Development-Task 5-Digital Library Management System/
│   └── ...
│
├── .gitignore
└── README.md
```

---

## Concepts Demonstrated

Across these projects, the repository demonstrates:

* Java fundamentals
* Object-Oriented Programming
* Encapsulation
* Abstraction
* Inheritance
* Polymorphism
* SOLID principles
* Collections and `ArrayList`
* Exception/input validation
* Random number generation
* Console application development
* Modular class design
* Database integration
* Spring Boot
* HTML/CSS/JavaScript
* CRUD operations

---

## How to Run

### Number Guessing Game

Navigate to the project directory and compile the Java source files.

```bash
javac -d out src/**/*.java
```

Then run the main class:

```bash
java -cp out Main
```

### ATM Interface

From the ATM project directory:

```bash
javac -d bin src/*.java
```

Run:

```bash
java -cp bin Main
```

### Digital Library Management System

Open the project in an IDE such as **IntelliJ IDEA, Eclipse, or VS Code**, configure the required MySQL database, and run the Spring Boot application.

---

## Development Practices

The projects are organized into separate classes and responsibilities to improve maintainability and readability.

The ATM Interface particularly follows the **SOLID design principles** by separating account management, banking operations, menu actions, transaction handling, and application flow into dedicated classes.

Compiled Java files such as `.class` files are excluded from version control through `.gitignore`.

---

## Author

**Subham Sahoo**

GitHub: [@subhamsahoo-4](https://github.com/subhamsahoo-4)

Repository: [OIBSIP](https://github.com/subhamsahoo-4/OIBSIP)

---

## Internship

**Oasis Infobyte - OIBSIP**

Java Development Internship
