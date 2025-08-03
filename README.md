# Financial Tracking Management System 🎊🎊🎊

## Prerequisites 🧨🧨

Before you begin, ensure you have met the following requirements:

- Java 24
- MySQL or another supported database

## Installation 🧨🧨

### Steps

**Database connection**

Location copy file from `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
```
server.host=localhost
server.port=9000
server.servlet.session.tracking-modes=cookie

spring.application.name=ftms-java-spring-boot
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

server.servlet.session.tracking-modes=cookie

ftms.email=admin@ftms.com
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=admin@ftms.com
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Following commands**
- Start server: `./mvnw spring-boot:run`
- Server running on: http://localhost:9000

## ✨🎉 Great! Let's go to use Financial Tracking Management System! 🎉✨

**Following users**
- Email: `admin@example.com` | Password: `123`
