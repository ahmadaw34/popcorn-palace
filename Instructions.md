# 🍿 Popcorn Palace – Setup & Run Instructions
Welcome to the Popcorn Palace project! This guide will help you build, run, and test the application locally using Maven and Spring Boot.

### ✅ Prerequisites
Before you begin, make sure you have the following installed:
* Java
* Maven
* PostgreSQL
* Git (optional, for cloning)
* Postman (optional as vscode extension)

### 📦 Project Setup
1. Clone the Repository
```
git clone https://github.com/ahmadaw34/popcorn-palace.git
cd popcorn-palace
```

2. Configure PostgreSQL
* Ensure PostgreSQL is installed and running on your local machine.
* Create a database named popcorn-palace

3. Update Database Credentials
* Open the configuration file:
```
popcorn-palace\src\main\resources\application.yaml
```
* And update the spring.datasource.username and spring.datasource.password fields with your local PostgreSQL credentials

4. Check Port Availability
* check port 8080 availability.

### 🚀 Running the Application
* Use Maven to start the application:
```
mvn spring-boot:run
```
* You can test this endpoints as described in readme using Postman

### 🧪 Running Tests
To execute tests, run:
```
mvn test 
```

