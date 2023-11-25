### Hexlet tests and linter status:
[![Actions Status](https://github.com/Konstantin-GIT/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/Konstantin-GIT/java-project-73/actions)

[![Maintainability](https://api.codeclimate.com/v1/badges/e2123c5557586466a233/maintainability)](https://codeclimate.com/github/Konstantin-GIT/java-project-73/maintainability)

[![Test Coverage](https://api.codeclimate.com/v1/badges/e2123c5557586466a233/test_coverage)](https://codeclimate.com/github/Konstantin-GIT/java-project-73/test_coverage)

### Description
Task Manager is a task management system similar to http://www.redmine.org /. It allows you to set tasks, assign performers and change their statuses. 
Registration and authentication are required to work with the system. It implements the fundamental principles of modern website development using the MVC architecture: 
handling routing, request handlers, and templating, as well as interacting with a database through ORM.
Technologies involved include:
- Frontend (Bootstrap, CDN).
- Spring Boot Framework (Routing, Views).
- Database (ORM Hibernate, Migrations, Query Builders).
- Deployment (PaaS). HTTP (including request execution).
- Integration Testing. Logging.
- Linters, running tests, CI.

### Requirements:
Before using this application you must install and configure:
- JDK 20;
- Gradle 8.2

### Run
```bash
./gradlew run
```

### Example of a deployed website on Render.com:
<a href="https://deploy-java-project-73-taskmanager.onrender.com">Task Manager</a>