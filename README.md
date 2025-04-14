# UPIPaymentGatewaySpringBoot

A Java Spring Boot-based UPI Payment Gateway that enables secure QR-based transactions with user registration, role-based access control, and MySQL integration.
1. user registration with default role.
2. update role API for dynamic role changes.
3. generating UPI QR when role is admin.
4. Dynamic exception handling.
5. Endpoints are secured with springSecurity.
6. RBAC for UPI QR generation.
7. WAR deployable on external servers (e.g., **JBoss EAP 7.2**, Apache Tomcat)

## 🔧 Features

- ✅ UPI QR Code Generation (dynamic with expiry)
- 👥 User Registration & Role Management
- 🔐 Spring Security for Authentication & Authorization
- 📦 MySQL Database Integration using Spring Data JPA
- ✅ RESTful API endpoints

## 🛠 Tech Stack

- Java 8  
- Spring Boot  
- Spring Security  
- Spring Data JPA  
- MySQL  
- Lombok  
- JWT  
- ZXing (QR Code)  
- Maven

## Contribution
Contributions are welcome! Open an issue or submit a PR.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
