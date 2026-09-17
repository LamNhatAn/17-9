# cellphoneX - Project 1 - Manual Mapper va ModelMapper

Project doc lap cho bai tap Mapper DTO trong Spring Boot.

## Vi du 1: Mapping thu cong
- `ManualCategoryMapper` tu sao chep tung truong giua `Category` va `CategoryDto`.
- API: `GET/POST http://localhost:8081/api/manual/categories`

## Vi du 2: ModelMapper
- `ModelMapperConfig` dang ky bean `ModelMapper`.
- API: `GET/POST http://localhost:8081/api/modelmapper/categories`

## Chay project

```text
mvn spring-boot:run
```

Tao moi bang JSON:

```json
{"name":"May anh","description":"Thiet bi chup hinh"}
```
