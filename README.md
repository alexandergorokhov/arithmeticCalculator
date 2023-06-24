## Arithmetic calculator
# Requirements
Java -17 (open jdk)
Maven  -3.8.2
Generate api key and token from https://api.random.org/
# How to build
mvn clean install
# How to run
mvn spring-boot:run -Dspring-boot.run.arguments="--random.api.key={"api key"} --random.api.url=https://api.random.org/json-rpc/4/invoke"
Or you can use your favorite IDE to run the application providing 2 environment variables:
random.api.key
random.api.url
# How to use
The application will run on port 8080.
For all of the endpoints you must provide Authorization header with the value of the token received from the login endpoint.

/api/v1/user/login - POST - body: {"username":"admin","password":"admin"}

/api/v1/record -  Pageable GET - returns a list of records. Params PageNumber and PageSize.
/api/v1/record - DELETE - deletes a record by id. Param: recordId

/api/v1/calculator/arithmeticOperation - POST- body: {"operand1":1,"operand2":2,"operationId":"1"} - returns the result of the operation.

OperationId:
1 - Addition
2 - Subtraction
3 - Multiplication
4 - Division
5 - Square root
6 - Random string

## 1
Generate api token and key from https://api.random.org/json-rpc/4/
## 2
Login using preconfigured username:test@domain.com , password: 123
ex: POST http://localhost:8080/api/v1/user/login
{"username":"test@domain.com","password":"123"}
## 3
Perform arithmetic operation.
ex: POST http://localhost:8080/api/v1/calculator/arithmeticOperation
Authorization : Bearer {token}
{"operand1":1,"operand2":2,"operationId":"1"}
## 4
Retrieve records.
Authorization : Bearer {token}
ex: GET http://localhost:8080/api/v1/record?pageNumber=0&pageSize=10
## 5
Delete a record.
Authorization : Bearer {token}
ex: DELETE http://localhost:8080/api/v1/record?recordId=1

# Testing
The application has unit tests and collaboration tests.
The scope of testing is to show the ability to write tests, not to cover all the code.

# Architecture
The application is built using layered architecture.

# Improvements
Introduction of AOP for Cross Cutting concerns  (logging).
Introduction of persistence layer for storing the data on a none in memory DB.
Introduction of caching layer for caching the data coming for the external API.
Introduction of circuit breaker for the external API.
Support for dynamic adding of new operations.

