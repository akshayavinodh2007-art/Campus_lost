# CampusFind – Lost & Found Tracker for Campus

A complete Spring Boot REST backend with an interactive web UI for tracking lost and found items on campus.

## 🚀 Features & Architecture

- **Clean Layered Architecture**: Entity → Repository → Service → Controller → DTO
- **User Roles**: `STUDENT`, `STAFF`, `ADMIN` (Registration & Login endpoints)
- **Categories**: Create and list item categories
- **Lost Item Reports**: Report and browse lost items with category, description, location, and date
- **Found Item Inventory**: Staff can register found items with statuses (`AVAILABLE`, `CLAIMED`, `RETURNED`)
- **Strict Business Rules (Enforced in Service Layer)**:
  - **Rule 1**: A found item cannot be marked `RETURNED` unless it is already `CLAIMED`. Direct `AVAILABLE → RETURNED` is rejected with `HTTP 400`.
  - **Rule 2**: Only `ADMIN` or the specific `STAFF` member who created the found item entry can change its status (Unauthorized attempts return `HTTP 403`).
- **Matching Feature**: Matches open lost reports with available found items based on Category, Location, and Description keywords (`/api/matching/lost-reports/{lostReportId}`).
- **Admin Dashboard**: Real-time counts of pending lost reports, claimed items, and returned items (`/api/admin/dashboard`).
- **Global Exception Handling**: `@ControllerAdvice` returning structured JSON error responses without stack traces.
- **Interactive UI**: Single-page frontend served directly at `http://localhost:8080/`.

---

## 🛠️ Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.2.4
- **Persistence**: Spring Data JPA / Hibernate
- **Database**: H2 (In-Memory default) / MySQL configurable in `application.properties`
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven

---

## 🏃 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/akshayavinodh2007-art/Campus_lost.git
cd Campus_lost
```

### 2. Build and run tests
```bash
./mvnw clean test
```

### 3. Run the application
```bash
./mvnw spring-boot:run
```

### 4. Access the application
- **Interactive Web UI**: [http://localhost:8080/](http://localhost:8080/)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:campusfind_db`, User: `sa`, Password: empty)

---

## 📡 REST API Summary

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register User (`STUDENT`, `STAFF`, `ADMIN`) |
| `POST` | `/api/users/login` | Login User |
| `POST` | `/api/categories` | Create Category |
| `GET` | `/api/categories` | List all Categories |
| `POST` | `/api/lost-reports` | Create Lost Report |
| `GET` | `/api/lost-reports` | List all Lost Reports |
| `POST` | `/api/found-items` | Create Found Item (Staff) |
| `GET` | `/api/found-items` | List all Found Items |
| `PATCH` | `/api/found-items/{id}/status` | Update Found Item Status |
| `GET` | `/api/matching/lost-reports/{id}` | Find matches for a lost report |
| `GET` | `/api/admin/dashboard` | Get dashboard summary counts |
