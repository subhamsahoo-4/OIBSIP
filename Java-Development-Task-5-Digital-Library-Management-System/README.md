# Digital Library Management System — OIBSIP Task 5

A full-stack **Digital Library Management System** built using **Spring Boot, MySQL, HTML, CSS, and JavaScript**.

The application provides separate **Admin** and **User** workflows for managing books, members, issues, reservations, fines, notifications, and contact queries.

---

## Features

### Admin

- Admin registration with secure registration code
- Admin login with role-based access control
- Add, edit, and delete books
- View active issued books and due dates
- View, activate, and deactivate registered members
- View all fines
- Mark fines as paid
- View member contact/query messages
- View notifications

### User

- User registration and login
- Browse the library catalogue
- Search books by title
- Search books by author
- Filter books by category
- Issue available books
- Reserve unavailable books
- View active issues and due dates
- Return books
- Automatic overdue fine calculation at return time
- View paid and unpaid fines
- View reservations
- Submit contact/query messages
- View notifications

---

## Business Rules

### Book Issuing

- Loan period: **14 days** from the issue date.
- A user cannot issue the same book more than once while an active issue already exists.
- A book can only be issued when an available copy exists.
- Issuing a book decreases the available-copy count.

### Book Returning

- Returning a book increases the available-copy count.
- The issue record is marked as returned.
- Overdue fines are calculated automatically during the return operation.
- Fine calculation is handled through a dedicated strategy.

### Fine Calculation

The current implementation uses a flat-rate fine strategy:

```text
₹5 per overdue day
```

Rules:

- Fine is calculated only when `returnBook()` is executed.
- There is no maximum fine cap.
- No fine is generated when the book is returned on or before the due date.
- Admin users can mark outstanding fines as paid.

### Reservations

- Users can reserve a book when no copy is currently available.
- A user cannot create duplicate active reservations for the same book.
- Reservations are processed in **FIFO order**.
- When a returned book becomes available, the oldest eligible reservation is fulfilled automatically.
- The fulfilled reservation is converted into a new 14-day issue.

### Book Management

- Books use soft deletion.
- A book cannot be deleted while copies are currently issued.
- Book quantity cannot be reduced below the number of currently issued copies.

### Member Management

- Members can be activated or deactivated by an administrator.
- Inactive members cannot authenticate or perform protected library operations.

---

## Technology Stack

### Backend

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- BCrypt password hashing
- Maven

### Database

- MySQL 8+

### Frontend

- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

### Testing

- JUnit 5
- Mockito
- Spring Boot Test

---

## Architecture

The application follows a layered architecture:

```text
Frontend
    │
    ▼
REST Controllers
    │
    ▼
Service Layer
    │
    ▼
Repository Layer
    │
    ▼
MySQL Database
```

### Controller Layer

Handles HTTP requests and REST responses.

Main controllers include:

```text
AuthController
BookController
MemberController
IssueController
FineController
ReservationController
ContactController
NotificationController
```

### Service Layer

Contains the application's business logic.

Main services include:

```text
AuthService
BookService
MemberService
IssueService
FineService
ReservationService
ContactService
NotificationService
```

### Repository Layer

Provides database access using Spring Data JPA.

### Entity Layer

Contains the persistent domain models, including:

```text
Member
Book
IssueRecord
Fine
Reservation
ContactQuery
Notification
```

---

## Project Structure

```text
task5-library-management/
├── pom.xml
├── schema.sql
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/
│   │   │       ├── LibraryApplication.java
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   └── MemberStatusFilter.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── BookController.java
│   │   │       │   ├── MemberController.java
│   │   │       │   ├── IssueController.java
│   │   │       │   ├── FineController.java
│   │   │       │   ├── ReservationController.java
│   │   │       │   ├── ContactController.java
│   │   │       │   └── NotificationController.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── BookService.java
│   │   │       │   ├── MemberService.java
│   │   │       │   ├── IssueService.java
│   │   │       │   ├── FineService.java
│   │   │       │   ├── FineCalculationStrategy.java
│   │   │       │   ├── ReservationService.java
│   │   │       │   ├── ContactService.java
│   │   │       │   ├── NotificationService.java
│   │   │       │   │
│   │   │       │   └── impl/
│   │   │       │       ├── AuthServiceImpl.java
│   │   │       │       ├── BookServiceImpl.java
│   │   │       │       ├── MemberServiceImpl.java
│   │   │       │       ├── IssueServiceImpl.java
│   │   │       │       ├── FineServiceImpl.java
│   │   │       │       ├── FlatRateFineStrategy.java
│   │   │       │       ├── ReservationServiceImpl.java
│   │   │       │       ├── ContactServiceImpl.java
│   │   │       │       └── NotificationServiceImpl.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       └── exception/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── forgot-password.html
│   │           │
│   │           ├── admin/
│   │           ├── user/
│   │           ├── css/
│   │           │   └── style.css
│   │           └── js/
│   │
│   └── test/
│       └── java/
│           └── com/library/
```

---

## Requirements

Install the following before running the project:

- **Java 17+**
- **MySQL 8+**
- **Maven 3.9+**

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## Database Setup

### 1. Start MySQL

Make sure the MySQL server is running.

### 2. Create the Database

```sql
CREATE DATABASE library_db;
```

Alternatively, use the provided `schema.sql` file to initialize the database structure.

### 3. Configure Database Connection

The application supports environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_password
```

The corresponding Spring Boot configuration is:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

JPA/Hibernate manages the application tables.

---

## Admin Registration

The application **does not create a fixed/demo administrator account automatically**.

A new administrator can register from:

```text
http://localhost:8080/register.html
```

Select:

```text
Account Type → Administrator
```

An **Admin Registration Code** is required to create an `ADMIN` account.

### Configure the Admin Registration Code

Set the following environment variable:

```text
ADMIN_REGISTRATION_CODE=your_secure_admin_registration_code
```

### Windows Command Prompt

```cmd
set ADMIN_REGISTRATION_CODE=your_secure_admin_registration_code
```

### Windows PowerShell

```powershell
$env:ADMIN_REGISTRATION_CODE="your_secure_admin_registration_code"
```

The application reads the configuration using:

```properties
app.security.admin-registration-code=${ADMIN_REGISTRATION_CODE:Admin@12345}
```

For local development, the default registration code is:

```text
Admin@12345
```

For production, configure your own strong value through the environment.

**Do not commit production registration codes or other secrets to GitHub.**

### Normal User Registration

Regular users do not need an admin registration code.

Select:

```text
Account Type → User
```

and complete the registration form.

---

## Registration API

### User Registration

```http
POST /api/auth/register
```

Example:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

### Admin Registration

```http
POST /api/auth/register
```

Example:

```json
{
  "name": "Library Admin",
  "email": "admin@example.com",
  "password": "securePassword123",
  "role": "ADMIN",
  "adminRegistrationCode": "your_secure_admin_registration_code"
}
```

The backend validates the admin registration code before creating an `ADMIN` account.

---

## Authentication and Authorization

The application uses **Spring Security** with session-based authentication.

### Roles

```text
ROLE_USER
ROLE_ADMIN
```

Administrative resources are protected using role-based authorization.

Examples:

```text
/admin/**
/api/admin/**
```

User resources are protected for authenticated users with the appropriate role.

Authentication state is maintained through an HTTP session.

The application also checks member status so inactive/deactivated members cannot continue using protected operations.

---

## API Overview

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register user or admin |
| POST | `/api/auth/login` | Public | Login |
| GET | `/api/auth/me` | Authenticated | Get current member |
| POST | `/api/auth/logout` | Authenticated | Logout |
| POST | `/api/auth/forgot-password` | Public | Request password reset |
| POST | `/api/auth/reset-password` | Public | Reset password |
| GET | `/api/books` | Public | Browse/search catalogue |
| POST | `/api/books` | Admin | Add book |
| PUT | `/api/books/{id}` | Admin | Edit book |
| DELETE | `/api/books/{id}` | Admin | Delete book |
| GET | `/api/user/issues` | User | View active issues |
| POST | `/api/user/issues/{bookId}` | User | Issue book |
| POST | `/api/user/issues/{issueId}/return` | User | Return book |
| GET | `/api/admin/issues` | Admin | View issued books |
| GET | `/api/user/fines` | User | View own fines |
| GET | `/api/admin/fines` | Admin | View all fines |
| PATCH | `/api/admin/fines/{id}/paid` | Admin | Mark fine paid |
| POST | `/api/user/reservations/{bookId}` | User | Reserve unavailable book |
| GET | `/api/user/reservations` | User | View reservations |
| POST | `/api/user/contact` | User | Submit contact/query |
| GET | `/api/admin/contact` | Admin | View contact inbox |
| GET | `/api/user/notifications` | User | View notifications |
| GET | `/api/admin/notifications` | Admin | View admin notifications |

---

## Application Workflow

### User Registration

```text
Register
   ↓
Select User
   ↓
Enter details
   ↓
Validate details
   ↓
Hash password with BCrypt
   ↓
Create USER account
```

### Admin Registration

```text
Register
   ↓
Select Administrator
   ↓
Enter Admin Registration Code
   ↓
Validate registration code
   ↓
Hash password with BCrypt
   ↓
Create ADMIN account
```

### Book Issue

```text
User selects book
       ↓
Check active book
       ↓
Check active member
       ↓
Check availability
       ↓
Check duplicate issue
       ↓
Create issue record
       ↓
Decrease available copies
       ↓
Set due date = issue date + 14 days
```

### Book Return

```text
User returns book
       ↓
Mark issue as returned
       ↓
Increase available copies
       ↓
Calculate overdue days
       ↓
Calculate fine
       ↓
Save fine if applicable
       ↓
Check reservations
       ↓
Fulfill oldest eligible reservation
```

---

## Fine Calculation Strategy

The application uses the **Strategy Design Pattern** for fine calculation.

Strategy interface:

```text
FineCalculationStrategy
```

Current implementation:

```text
FlatRateFineStrategy
```

Current rate:

```text
₹5 per overdue day
```

This design allows additional fine-calculation strategies to be introduced without modifying the core return workflow.

Possible future strategies include:

```text
Flat-rate fine
Percentage-based fine
Member-type based fine
Holiday-aware fine
```

---

## Notifications

The application supports database-backed notifications for important events.

Examples include:

- Successful registration
- Book issue
- Book return
- Fine creation
- Fine payment
- Reservation fulfillment
- Contact/query activity
- Administrative member events

Notifications can be accessed through the appropriate user or admin interface.

---

## Soft Deletion

The application uses soft deletion for important records such as books and members.

Instead of immediately removing records from the database, the application maintains an active/inactive state.

Benefits include:

- Preservation of historical records
- Better data integrity
- Safer member management
- Prevention of broken references
- Preservation of issue history

Inactive books are excluded from the public catalogue.

Inactive members cannot authenticate or perform protected library operations.

---

## Validation

The application validates important business constraints, including:

- Required member information
- Email format
- Minimum password length
- Duplicate email addresses
- Duplicate ISBN values
- Book quantity
- Available copies
- Active member status
- Active book status
- Duplicate active issues
- Duplicate active reservations
- Fine payment state
- Admin registration code

---

## Exception Handling

The backend uses centralized exception handling through:

```text
GlobalExceptionHandler
```

Application-specific exceptions include:

```text
BookUnavailableException
ResourceNotFoundException
```

This allows errors to be handled consistently and returned to the frontend through REST responses.

---

## Testing

The project includes unit and controller tests using:

- JUnit 5
- Mockito
- Spring Boot Test

Current test classes include:

```text
BookControllerTest
BookServiceTest
IssueServiceTest
ReservationServiceTest
FineServiceTest
```

Run the complete test suite:

```bash
mvn clean test
```

---

## Running the Application

### 1. Start MySQL

Make sure MySQL is running.

### 2. Configure Database Credentials

Example using Windows Command Prompt:

```cmd
set DB_USERNAME=root
set DB_PASSWORD=your_password
```

### 3. Configure Admin Registration Code

For local development:

```cmd
set ADMIN_REGISTRATION_CODE=Admin@12345
```

Or use your own code:

```cmd
set ADMIN_REGISTRATION_CODE=your_secure_admin_registration_code
```

PowerShell:

```powershell
$env:ADMIN_REGISTRATION_CODE="your_secure_admin_registration_code"
```

**Set environment variables before starting Spring Boot.**

### 4. Run the Application

```bash
mvn spring-boot:run
```

### 5. Open the Application

```text
http://localhost:8080/
```

or:

```text
http://localhost:8080/login.html
```

### 6. Register an Account

Open:

```text
http://localhost:8080/register.html
```

Choose either **User** or **Administrator**.

Administrators must provide the configured registration code.

---

## Maven Commands

### Clean and Compile

```bash
mvn clean compile
```

### Run Tests

```bash
mvn clean test
```

### Package the Application

```bash
mvn clean package
```

### Run the Application

```bash
mvn spring-boot:run
```

---

## Configuration

Important application configuration is stored in:

```text
src/main/resources/application.properties
```

Database:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

Admin registration:

```properties
app.security.admin-registration-code=${ADMIN_REGISTRATION_CODE:Admin@12345}
```

Email configuration can be supplied through environment variables:

```text
MAIL_USERNAME=your_email
MAIL_PASSWORD=your_app_password
```

Sensitive credentials should not be committed to source control.

---

## Security

The application implements:

- Spring Security authentication
- Role-based authorization
- BCrypt password hashing
- Session-based authentication
- Admin registration-code protection
- Active/inactive member validation
- Protected admin endpoints
- Protected user endpoints
- Environment-based database credentials
- Environment-based admin registration code

### Production Recommendations

For production deployment, consider adding:

- HTTPS
- CSRF protection
- Rate limiting
- Brute-force protection
- Account lockout
- Stronger password policies
- Secure password-reset tokens
- Email verification
- Secret-management services
- Audit logging
- Security headers
- Multi-factor authentication
- Centralized logging and monitoring
- Database-level constraints for concurrent operations

---

## Concurrency Considerations

For production-scale deployments, concurrent library operations should be protected using appropriate database transactions and locking.

Important areas include:

- Simultaneous issue requests for the last available copy
- Concurrent reservation requests
- Concurrent book quantity updates
- Reservation fulfillment

Database constraints and transactional locking can help guarantee consistency under concurrent requests.

---

## Development Workflow

A typical development workflow is:

```text
Modify source code
      ↓
Run unit tests
      ↓
Run application
      ↓
Test frontend/API
      ↓
Review changes
      ↓
Commit changes
      ↓
Push to GitHub
```

Recommended test command:

```bash
mvn clean test
```

Then run the application:

```bash
mvn spring-boot:run
```

---

## Screenshots

Recommended screenshots to add to the repository:

1. Login page
2. User registration page
3. Admin registration page
4. Admin dashboard
5. Manage books
6. Manage members
7. Issued books
8. Fine management
9. User dashboard
10. Book catalogue
11. Reservations
12. My issues
13. Contact/query page

Example:

```markdown
![Login Page](screenshots/login.png)

![Admin Dashboard](screenshots/admin-dashboard.png)

![Book Catalogue](screenshots/catalogue.png)
```

---

## OIBSIP Task Information

**Program:** Oasis Infobyte Internship Program (OIBSIP)

**Domain:** Java Development

**Task:** Task 5 — Digital Library Management System

**Project Type:** Full-Stack Web Application

**Backend:** Java + Spring Boot

**Database:** MySQL

**Frontend:** HTML + CSS + JavaScript

---

## Author

**Subham Kumar Sahoo**

GitHub:

https://github.com/subhamsahoo-4

Project Repository:

https://github.com/subhamsahoo-4/OIBSIP

---

## License

This project was developed as part of the **OIBSIP Java Development Internship** and is intended primarily for educational, internship, and portfolio purposes.

---

## Conclusion

The Digital Library Management System provides a complete library-management workflow with separate Admin and User roles.

The system supports:

- User registration
- Admin registration
- Secure admin registration-code validation
- Role-based authentication
- Book catalogue management
- Book searching and filtering
- Book issuing
- Book returning
- 14-day loan periods
- Automatic overdue fine calculation
- ₹5-per-day fine calculation
- Fine payment management
- Book reservations
- FIFO reservation fulfillment
- Member management
- Notifications
- Contact/query management
- Soft deletion
- REST APIs
- Centralized exception handling
- Unit and controller testing

The project demonstrates practical implementation of **Java 17, Spring Boot, Spring Security, Spring Data JPA, MySQL, REST APIs, session-based authentication, layered architecture, the Strategy Design Pattern, BCrypt password hashing, database persistence, and frontend-backend integration**.
