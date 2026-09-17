# Category App

Website quan ly Product va Category bang Spring Boot, Thymeleaf, AJAX, REST API va GraphQL.

## Cong nghe

- Java 25
- Spring Boot 3.5.6
- Spring Data JPA
- H2 Database
- Thymeleaf
- AJAX Fetch API
- Swagger 3 / OpenAPI
- GraphQL

## Chay ung dung

Yeu cau JDK 25 va Maven 3.9+.

```powershell
$env:JAVA_HOME='C:\Users\ADMIN\.jdk\jdk-25.0.2'
mvn spring-boot:run
```

## Cac duong dan chinh

- Website AJAX: http://localhost:8080/ajax
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- GraphQL endpoint: http://localhost:8080/graphql
- H2 Console: http://localhost:8080/h2-console

## Chuc nang

- CRUD Product va Category bang AJAX.
- Tim kiem Product va Category.
- Phan trang REST API.
- Loc Product theo Category.
- Hien thi Product theo gia tang dan.
- REST API co the kiem tra bang Swagger.
- GraphQL query va mutation cho Product/Category.

## Kiem tra

```powershell
mvn clean test
```
