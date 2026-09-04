# Digital Library Management System — Task 5

A full-stack **Digital Library Management System** developed using **Spring Boot, MySQL, HTML, CSS, and JavaScript**.

The application provides separate functionality and access control for **Admin** and **User** roles. It manages books, members, book issues, returns, reservations, fines, notifications, and contact queries through a REST-based backend and browser-based frontend.

---

## 📌 Project Overview

The Digital Library Management System is designed to automate common library operations such as:

- Managing books and their availability
- Registering and managing library members
- Issuing and returning books
- Calculating overdue fines
- Reserving unavailable books
- Managing member accounts
- Managing and tracking fines
- Handling user contact/query requests
- Sending application notifications
- Enforcing Admin/User role-based access control

The project follows a layered architecture using:

- Controllers
- Services
- Repositories
- Entities
- DTOs
- Exception handling
- Security configuration

---

# 🚀 Technology Stack

## Backend

- **Java 17**
- **Spring Boot**
- **Spring MVC**
- **Spring Security**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Maven**

## Frontend

- **HTML5**
- **CSS3**
- **JavaScript**
- **Fetch API**

## Testing

- **JUnit 5**
- **Mockito**
- **Spring Boot Test**

## Security

- **Spring Security**
- **BCrypt Password Encoding**
- **Session-based Authentication**
- **Role-Based Authorization**

---

# ✨ Features

## 👨‍💼 Admin Features

Administrators have access to library management operations.

### Authentication

- Admin login
- Session-based authentication
- Role-based authorization
- Logout functionality
- Inactive account prevention

### Book Management

- Add new books
- Edit existing books
- Soft-delete books
- View book catalogue
- Track total quantity
- Track available copies
- Prevent invalid quantity updates
- Prevent deletion of books with active issued copies

### Issue Management

- View issued books
- View issue dates
- View due dates
- Track returned and active issues

### Member Management

- View registered members
- Activate members
- Deactivate members
- Manage member status

### Fine Management

- View fines
- View unpaid fines
- View paid fines
- Mark fines as paid
- Track fine amount and payment status

### Contact Management

- View user contact/query messages
- Review submitted queries

---

# 👤 User Features

Registered users can access library services through the user dashboard.

### Authentication

- User registration
- User login
- Logout
- Session-based authentication
- Password encryption using BCrypt
- Account status validation

### Book Catalogue

- Browse available books
- Search books by title
- Search books by author
- Filter books by category
- View book availability

### Book Issue

- Issue available books
- View active issued books
- View issue dates
- View due dates

### Book Return

- Return issued books
- Automatically calculate overdue fines
- Update book availability
- Complete pending reservation fulfillment

### Reservations

- Reserve unavailable books
- View current reservations
- Prevent duplicate reservations
- FIFO-based reservation fulfillment

### Fines

- View fines
- View unpaid fines
- View paid fines
- View fine amounts
- Track payment status

### Contact

- Submit contact/query messages to the library administrator

### Notifications

Users receive notifications for relevant library events such as:

- Book issue
- Book return
- Reservation
- Reservation fulfillment
- Fine generation
- Fine payment
- Account-related events

---

# 📚 Business Rules

The application implements the following library rules.

### Loan Period

Every issued book has a loan period of:

**14 days**

The due date is calculated as:

```text
Due Date = Issue Date + 14 days
```
Fine Calculation

The overdue fine is:

₹5 per overdue day

The fine is:

Calculated only when the book is returned
Not capped at a maximum amount
Generated only when the book is returned late

Example:

Overdue Days = 3
Fine = 3 × ₹5
Fine = ₹15
Book Availability

When a book is issued:

Available Copies = Available Copies - 1

When a book is returned:

Available Copies = Available Copies + 1
Duplicate Issues

A user cannot have multiple active issues for the same book.

Reservations

A user cannot create duplicate active reservations for the same book.

FIFO Reservation

Reservations are fulfilled in:

First In → First Out

The oldest pending reservation is fulfilled first when a copy becomes available.

Reservation Fulfillment

When a reserved book becomes available:

The oldest pending reservation is selected.
The reservation is fulfilled.
A new issue record is created for the user.
The available copy is assigned to that user.
A new 14-day loan period begins.
The user is notified.
Book Deletion

Books use soft deletion.

A book cannot be deleted if it currently has issued copies.

Deleted/inactive books:

Do not appear in the normal catalogue
Cannot be issued
Cannot be reserved
Book Quantity

The total quantity of a book cannot be reduced below the number of currently issued copies.

Member Status

Inactive members cannot:

Log in
Issue books
Reserve books
Perform normal library operations
🏗️ Project Architecture

The application follows a layered Spring Boot architecture.

Frontend
   │
   ▼
Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
MySQL Database
Controller Layer

Handles HTTP requests and API endpoints.

Service Layer

Contains business logic and validation.

Repository Layer

Handles database operations using Spring Data JPA.

Entity Layer

Contains JPA entities representing database tables.

Security Layer

Handles:

Authentication
Authorization
Session management
User status validation
Role-based access
📁 Project Structure


Java Development-Task 5-Digital Library Management System/
│
├── pom.xml
├── schema.sql
├── README.md
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/
    │   │   │
    │   │   └── com/
    │   │       └── library/
    │   │           │
    │   │           ├── LibraryApplication.java
    │   │           │
    │   │           ├── config/
    │   │           │   ├── SecurityConfig.java
    │   │           │   ├── DataInitializer.java
    │   │           │   └── MemberStatusFilter.java
    │   │           │
    │   │           ├── controller/
    │   │           │   ├── AuthController.java
    │   │           │   ├── BookController.java
    │   │           │   ├── MemberController.java
    │   │           │   ├── IssueController.java
    │   │           │   ├── FineController.java
    │   │           │   ├── ReservationController.java
    │   │           │   └── ContactController.java
    │   │           │
    │   │           ├── service/
    │   │           │   ├── AuthService.java
    │   │           │   ├── BookService.java
    │   │           │   ├── MemberService.java
    │   │           │   ├── IssueService.java
    │   │           │   ├── FineService.java
    │   │           │   ├── FineCalculationStrategy.java
    │   │           │   ├── ReservationService.java
    │   │           │   ├── ContactService.java
    │   │           │   │
    │   │           │   └── impl/
    │   │           │       ├── AuthServiceImpl.java
    │   │           │       ├── BookServiceImpl.java
    │   │           │       ├── MemberServiceImpl.java
    │   │           │       ├── IssueServiceImpl.java
    │   │           │       ├── FineServiceImpl.java
    │   │           │       ├── FlatRateFineStrategy.java
    │   │           │       ├── ReservationServiceImpl.java
    │   │           │       └── ContactServiceImpl.java
    │   │           │
    │   │           ├── repository/
    │   │           │   ├── MemberRepository.java
    │   │           │   ├── BookRepository.java
    │   │           │   ├── IssueRecordRepository.java
    │   │           │   ├── FineRepository.java
    │   │           │   ├── ReservationRepository.java
    │   │           │   └── ContactQueryRepository.java
    │   │           │
    │   │           ├── entity/
    │   │           │   ├── Member.java
    │   │           │   ├── Book.java
    │   │           │   ├── IssueRecord.java
    │   │           │   ├── Fine.java
    │   │           │   ├── Reservation.java
    │   │           │   └── ContactQuery.java
    │   │           │
    │   │           ├── dto/
    │   │           │   ├── request/
    │   │           │   └── response/
    │   │           │
    │   │           └── exception/
    │   │
    │   └── resources/
    │       │
    │       ├── application.properties
    │       │
    │       └── static/
    │           ├── login.html
    │           ├── register.html
    │           ├── forgot-password.html
    │           │
    │           ├── admin/
    │           │
    │           ├── user/
    │           │
    │           ├── css/
    │           │   └── style.css
    │           │
    │           └── js/
    │
    └── test/
        │
        └── java/
            └── com/
                └── library/
                    ├── controller/
                    └── service/


                    
⚙️ Requirements

Before running the project, install:

Java 17 or higher
Maven 3.9+
MySQL 8+
Git

Verify the installations:

java -version
mvn -version
mysql --version
🗄️ Database Setup
1. Create the Database

Open MySQL and run:

CREATE DATABASE library_db;

Select the database:

USE library_db;
2. Run the Database Schema

The project contains:

schema.sql

Execute it against the library_db database.

For example:

mysql -u root -p library_db < schema.sql
🔐 Database Configuration

The application uses environment variables for database credentials.

The default configuration expects:

DB_URL
DB_USERNAME
DB_PASSWORD

Example:

DB_URL=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
Windows PowerShell
$env:DB_URL="jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"

Do not commit real database credentials to GitHub.

📧 Email Configuration

The application supports email-related functionality using configured mail credentials.

Set the following environment variables:

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password

For Gmail, use a Google App Password rather than your normal Gmail password.

Do not commit email credentials to the repository.

▶️ Running the Application
Option 1 — Maven

From the project directory:

mvn spring-boot:run
Option 2 — Build and Run

Build the project:

mvn clean package

Then run the generated JAR:

java -jar target/<generated-jar-name>.jar
🌐 Access the Application

After starting the Spring Boot application, open:

http://localhost:8080/login.html

Registration page:

http://localhost:8080/register.html
👨‍💼 Demo Admin Account

For local/demo purposes, the application initializes an administrator account.

Email:    admin@library.com
Password: admin123
Role:     ADMIN

⚠️ Important

This account is intended only for development/demo purposes.

For production deployment:

Change the default password
Use a secure password
Store credentials securely
Do not expose default credentials
🔑 Authentication & Authorization

The application uses Spring Security for authentication and authorization.

Two primary roles are supported:

ROLE_ADMIN
ROLE_USER
Admin Access

Admin endpoints are protected using:

/admin/**
/api/admin/**
User Access

User endpoints are protected using:

/user/**
/api/user/**

Unauthenticated users can access public resources such as:

/login.html
/register.html
/forgot-password.html
/api/auth/**
🔒 Password Security

User passwords are not stored as plain text.

Passwords are encrypted using:

BCryptPasswordEncoder

Authentication verifies the submitted password against the BCrypt hash stored in the database.

🔄 Library Workflow
Book Issue Workflow
User Login
    ↓
Browse Catalogue
    ↓
Select Available Book
    ↓
Issue Book
    ↓
Available Copies - 1
    ↓
Issue Record Created
    ↓
Due Date = Issue Date + 14 Days
Book Return Workflow
User Returns Book
       ↓
Issue Marked as Returned
       ↓
Available Copies + 1
       ↓
Check Due Date
       ↓
Calculate Overdue Days
       ↓
Calculate Fine
       ↓
Save Fine
       ↓
Check Pending Reservations
       ↓
Fulfill Oldest Reservation
📖 Reservation Workflow

When no copies are available:

User
 ↓
Reserve Book
 ↓
Reservation Added
 ↓
Waiting Queue

When a copy becomes available:

Book Available
      ↓
Find Oldest Pending Reservation
      ↓
Verify Member Status
      ↓
Create Issue
      ↓
Fulfill Reservation
      ↓
Notify User

Reservations are processed using FIFO ordering.

💰 Fine Calculation Strategy

The application uses a strategy-based fine calculation design.

The fine calculation is represented by:

FineCalculationStrategy

The current implementation includes:

FlatRateFineStrategy

The current fine rate is:

₹5 per overdue day

This design allows additional fine calculation strategies to be introduced in the future without significantly changing the issue/return business logic.

🧪 Testing

The project uses:

JUnit 5
Mockito
Spring Boot Test

The test suite covers important application behavior including:

Book Tests
Book creation
Book validation
Duplicate ISBN validation
Book update
Quantity validation
Soft deletion
Active book filtering
Issue Tests
Issue date calculation
Due date calculation
Book availability
Duplicate active issue prevention
Book return
Fine calculation
Inactive member handling
Reservation Tests
Reservation creation
Duplicate reservation prevention
FIFO reservation fulfillment
Inactive member handling
Reservation state changes
Fine Tests
Fine calculation
Fine payment
Duplicate payment prevention
Controller Tests
Book API behavior
Request validation
HTTP responses
Authorization-related behavior
✅ Test Results

The current project test suite contains:

27 Tests
27 Passed
0 Failures
0 Errors

Test distribution:

BookControllerTest       → 5 tests
BookServiceTest          → 8 tests
IssueServiceTest         → 7 tests
ReservationServiceTest   → 5 tests
FineServiceTest          → 2 tests
---------------------------------
Total                    → 27 tests

Run all tests using:

mvn clean test
🔌 API Overview
Authentication APIs
Register
POST /api/auth/register
Login
POST /api/auth/login
Current User
GET /api/auth/me
Logout
POST /api/auth/logout
📚 Book APIs
Get Books
GET /api/books
Get Book
GET /api/books/{id}
Add Book
POST /api/books
Update Book
PUT /api/books/{id}
Delete Book
DELETE /api/books/{id}
📕 Issue APIs
Get User Issues
GET /api/user/issues
Issue Book
POST /api/user/issues/{bookId}
Return Book
POST /api/user/issues/{issueId}/return
Get Admin Issues
GET /api/admin/issues
💰 Fine APIs
Get User Fines
GET /api/user/fines
Get Admin Fines
GET /api/admin/fines
Mark Fine as Paid
PATCH /api/admin/fines/{id}/paid
📌 Reservation APIs
Reserve Book
POST /api/user/reservations/{bookId}
Get User Reservations
GET /api/user/reservations
📩 Contact APIs
Submit Contact Query
POST /api/user/contact
View Contact Queries
GET /api/admin/contact
🛡️ Security Features

The application includes several security mechanisms:

Spring Security integration
Session-based authentication
Role-based authorization
BCrypt password hashing
Active/inactive member validation
Protected Admin endpoints
Protected User endpoints
Unauthorized API responses
Access-denied handling
Session invalidation for inactive users
Environment-based credential configuration
🗑️ Soft Delete

Books are not physically removed from the database when deleted.

Instead, their active status is changed:

active = false

This preserves historical records such as:

Previous issues
Returns
Fines
Reservations
Related database relationships

Inactive books are excluded from normal catalogue operations.

🔔 Notifications

The application maintains notifications for important library events.

Notifications include:

Issue notifications
Return notifications
Reservation notifications
Reservation fulfillment
Fine notifications
Fine payment notifications
Account-related notifications

Notifications track whether they have been read.

🧩 Exception Handling

The application uses centralized exception handling for common business errors such as:

Resource not found
Invalid requests
Duplicate operations
Invalid book quantities
Unavailable books
Inactive members
Invalid issue/return operations
Already-paid fines

This allows the REST API to return meaningful HTTP responses instead of exposing internal application errors.

📦 Maven Commands

Compile the project:

mvn compile

Run tests:

mvn test

Clean and test:

mvn clean test

Package the application:

mvn clean package

Run the application:

mvn spring-boot:run
🧑‍💻 Development Workflow

A typical development workflow is:

Clone Repository
      ↓
Configure MySQL
      ↓
Create library_db
      ↓
Run schema.sql
      ↓
Configure Environment Variables
      ↓
Run Maven Application
      ↓
Open Login Page
      ↓
Register/Login
      ↓
Use Library Features
📂 Important Files
File	Purpose
pom.xml	Maven dependencies and project configuration
schema.sql	Database schema
application.properties	Application/database/mail configuration
LibraryApplication.java	Spring Boot entry point
SecurityConfig.java	Spring Security configuration
BookController.java	Book REST APIs
IssueController.java	Issue/return APIs
FineController.java	Fine APIs
ReservationController.java	Reservation APIs
ContactController.java	Contact/query APIs
BookServiceImpl.java	Book business logic
IssueServiceImpl.java	Issue/return business logic
FineServiceImpl.java	Fine management
ReservationServiceImpl.java	Reservation and FIFO fulfillment
FlatRateFineStrategy.java	Fine calculation strategy
⚠️ Production Considerations

This project is primarily intended for educational, demonstration, and internship-task purposes.

Before production deployment, additional hardening should be considered, including:

Enable and properly configure CSRF protection where applicable
Use HTTPS
Add rate limiting for authentication/password-reset endpoints
Use stronger password policies
Externalize and securely manage all secrets
Add comprehensive audit logging
Configure secure session settings
Add database-level concurrency controls
Add unique database constraints where required
Improve validation for malformed requests
Prevent account deactivation while active loans exist, or provide an appropriate administrative return workflow
Avoid default/demo credentials in production
🐛 Known Engineering Considerations

The application has been tested for the core business flows. For production-scale usage, concurrency controls should be added around operations such as:

Simultaneous book issue requests
Simultaneous duplicate reservations
Concurrent availability updates

Database-level locking or atomic update strategies can be used to guarantee consistency under high concurrency.

📸 Suggested Screenshots for GitHub

For a stronger project presentation, screenshots can be added to the repository showing:

Login page
Registration page
Admin dashboard
Book management
Member management
Fine management
User dashboard
Book catalogue
Issue/return section
Reservation section
Contact/query form

Example:

## Screenshots

### Login
![Login](screenshots/login.png)

### Admin Dashboard
![Admin Dashboard](screenshots/admin-dashboard.png)

### User Dashboard
![User Dashboard](screenshots/user-dashboard.png)
📌 OIBSIP Task Information

This project was developed as part of the:

Oasis Infobyte Internship Program (OIBSIP)

Task
Java Development
Task 5
Digital Library Management System
👨‍💻 Author

Subham Kumar Sahoo

GitHub:

https://github.com/subhamsahoo-4

Repository:

https://github.com/subhamsahoo-4/OIBSIP
📜 License

This project was developed for educational and internship purposes as part of the OIBSIP Java Development Task 5.

⭐ Project Summary

The Digital Library Management System provides a complete library workflow with:

Authentication
     +
Role-Based Authorization
     +
Book Management
     +
Member Management
     +
Book Issue & Return
     +
Automatic Fine Calculation
     +
Reservations
     +
FIFO Fulfillment
     +
Notifications
     +
Contact Management
     +
MySQL Persistence
     +
Automated Testing

The project demonstrates practical implementation of Spring Boot REST APIs, Spring Security, JPA/Hibernate, MySQL, layered architecture, business-rule enforcement, design patterns, and unit testing.


git add .
git commit -m "Update Digital Library Management System README"
git push origin main
