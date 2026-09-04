# Digital Library Management System — Task 5

Spring Boot + MySQL + HTML/CSS/JavaScript implementation of the required Digital Library Management System.

## Features

### Admin
- Login with admin role
- Add, edit and delete books
- View active issued books and due dates
- View/activate/deactivate registered members
- View fines and mark them paid
- View member contact/query messages

### User
- Register and login
- Browse/search catalogue by title, author and category
- Issue available books
- Reserve unavailable books
- View active issues and due dates
- Return books
- Automatic overdue fine at return time: **₹5 per overdue day**
- Automatic FIFO reservation fulfillment during return
- View paid/unpaid fines
- Submit contact/query messages

## Business rules

- Loan period: **14 days** from issue date.
- Fine: **₹5 per day**, calculated only when `returnBook()` is executed.
- Fine has no maximum cap.
- If a returned book has a waiting reservation, the oldest unfulfilled reservation is converted immediately into a new 14-day issue.
- A user cannot duplicate an active issue or reservation for the same book.
- A book cannot be deleted while copies are currently issued.
- Book quantity edits cannot reduce total quantity below the number of currently issued copies.

## Project structure

```text
task5-library-management/
├── pom.xml
├── schema.sql
├── src/main/java/com/library/
│   ├── LibraryApplication.java
│   ├── config/
│   ├── controller/
│   ├── service/
│   │   └── impl/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   └── exception/
└── src/main/resources/
    ├── application.properties
    └── static/
        ├── login.html
        ├── register.html
        ├── admin/
        ├── user/
        ├── css/style.css
        └── js/
```

## Requirements

- Java 17+
- MySQL 8+
- Maven 3.9+

## Database setup

1. Start MySQL.
2. Run `schema.sql` or create a database named `library_db`.
3. Update `src/main/resources/application.properties`, or set:

```text
DB_URL=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=${DB_USERNAME:root}
DB_PASSWORD=${DB_PASSWORD}
```

JPA creates the application tables automatically.

## Run

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080/login.html
```

## Demo admin

```text
Email: admin@library.com
Password: admin123
```

The admin account is created automatically on first application startup if it does not already exist.

## API overview

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register user |
| POST | `/api/auth/login` | Public | Login |
| GET | `/api/auth/me` | Authenticated | Current member |
| POST | `/api/auth/logout` | Authenticated | Logout |
| GET | `/api/books` | Public | Catalogue/search |
| POST | `/api/books` | Admin | Add book |
| PUT | `/api/books/{id}` | Admin | Edit book |
| DELETE | `/api/books/{id}` | Admin | Delete book |
| GET | `/api/user/issues` | User | Active issues |
| POST | `/api/user/issues/{bookId}` | User | Issue book |
| POST | `/api/user/issues/{issueId}/return` | User | Return book + fine/reservation logic |
| GET | `/api/admin/issues` | Admin | Active issued books |
| GET | `/api/user/fines` | User | My fines |
| GET | `/api/admin/fines` | Admin | All fines |
| PATCH | `/api/admin/fines/{id}/paid` | Admin | Mark fine paid |
| POST | `/api/user/reservations/{bookId}` | User | Reserve unavailable book |
| GET | `/api/user/reservations` | User | My reservations |
| POST | `/api/user/contact` | User | Submit query |
| GET | `/api/admin/contact` | Admin | View inbox |

## Security note

The demo intentionally uses a simple session-based login flow so the frontend can work directly with the Spring Boot REST API. For production deployment, add CSRF protection, HTTPS, rate limiting, stronger password policy, audit logging, and externalized secret management.
