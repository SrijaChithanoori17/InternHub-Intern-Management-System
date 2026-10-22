# InternHub - Intern Management System

A full-stack application for managing interns and internship batches.

## Technology Stack

- Frontend: Angular, TypeScript, HTML, CSS
- Backend: Java, Spring Boot, Spring Data JPA
- Database: MySQL
- API: REST APIs

## Features

- Register and manage intern records
- Generate intern IDs
- Create and manage internship batches
- Search and filter intern records
- View batch details and assigned interns
- Update and delete intern records

## Project Structure

- `frontend/` - Angular application
- `backend/` - Spring Boot application

## Prerequisites

- Java JDK
- Node.js and npm
- MySQL

## Database Setup

1. Start MySQL.
2. Create a database named `intern_management`.
3. Set the `DB_USERNAME` and `DB_PASSWORD` environment variables with your MySQL credentials.

## Run the Backend

Open PowerShell inside the `backend` directory:

```powershell
$env:DB_USERNAME="your_mysql_username"
$env:DB_PASSWORD="your_mysql_password"
.\mvnw.cmd spring-boot:run
```

The backend uses port 8080 by default.

## Run the Frontend

Open a separate terminal inside the `frontend` directory:

```bash
npm install
npm start
```

## Security

Never commit database passwords, API keys, or other secrets to GitHub.
