<p style="text-align: center"><img src="./assets/Shiitake-Default-1024x1024@2x.png" alt="emotio Hero Banner" width="300"/></p>

# Shiitake - FoodWaste Conscious Ordering System
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

## Description

Shiitake is a backend platform for a FoodWaste Conscious Ordering System that manages accounts creation, businesses enrollment order requests and many more.
This repository represents only the Backend of the Shiitake System composed of 3 main components.
## Setup

1. Clone the repository:

` git clone https://github.com/xatyy/shiitake.git `

2. Navigate to the project directory:

` cd shiitake `


3. Build the project:

 `./mvnw clean install`

> [!important]
> For this Backend to be fully operable an AWS account is required for database saving, email sending and media usage. This project is making use of the following services: S3 Bucket, Redis and SES.

Inside `application-prod.properites` and `application-dev.properites` the following lines must be changed:

```properties
spring.datasource.url= <db url>
spring.datasource.username= <db username>
spring.datasource.password= <db password>

spring.cloud.aws.credentials.access-key= <AWS Public key>
spring.cloud.aws.credentials.secret-key= <AWS Secret key>

custom.aws.s3.endpoint=https://<your own url>.eu-north-1.amazonaws.com
custom.aws.ses.endpoint=https://<your own url>.eu-north-1.amazonaws.com
```
## Project Structure

```
├── BackendApplication.java
├── configuration        # App configuration (AWS, security, Swagger, WebSocket)
├── controller           # REST API controllers
├── dto                  # Data Transfer Objects
├── exceptions           # Custom exceptions and error handling
├── mapper               # Model-to-DTO mappers
├── model                # JPA entities / data models
├── repository           # Database repositories
├── service              # Business logic services
├── schedule             # Scheduled tasks
├── security             # Security configuration and JWT
└── utils                # Utility classes
```

* This project follows SOLID principles, layered architecture, DRY, IoC/DI, modularization, and exception handling.

## Project Roadmap

- [ ] Finish Shiitake Frontend.
- [ ] Dockerize Full Project.
- [ ] Create the mobile app to connect to Shiitake System.
---
<p style="text-align: center;">Made with <3 by <a href="https://github.com/xatyy/">xaty</a>.</p>

