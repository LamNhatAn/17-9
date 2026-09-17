# cellphoneX - Project 2 - MapStruct

Project doc lap cho Vi du 3: mapping DTO bang MapStruct trong Spring Boot.

## Cau truc

- `Category`: JPA Entity.
- `CategoryDto`: DTO dung tai API.
- `CategoryMapper`: interface `@Mapper(componentModel = "spring")`.
- Maven compiler plugin tu dong chay `mapstruct-processor` de sinh implementation.

## API

- `GET http://localhost:8082/api/categories`
- `POST http://localhost:8082/api/categories`

JSON tao moi:

```json
{"name":"May anh","description":"Thiet bi chup hinh"}
```

## Chay project

```text
mvn spring-boot:run
```
