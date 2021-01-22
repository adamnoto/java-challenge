# Employee Service

To run:

```bash
$ mvn spring-boot:run
```

What I did:

- Add validations to Employee (through DTO/Data Transfer Object)
- Add security layer via Authorization header. Extensible by use of `App` entity to represent "who can do what"
- Implement caching with Spring Cache and Hazelcast
- Added ControllerAdvisor to centralize exception handling for reporting validation errors
- Use logger (instead of System.out.println)
- Refactor the dependency injection by using Lombok
- Added tests (controller & a repository test)
- Introduce profiles to separate between test and development. Development database are persisted, rather than in-memory. Both are using H2 as the DBMS.
- Some refactoring (eg: using of Optional, making the request path easier to read, etc.)

If I have more time: I will authenticate each methods in the controller with the actual rights/privileges given to the App. It can be done, but... there're always features to add :)

## Possible things to do with the App:

- List the employees:

  > curl -u axa:12345 http://localhost:8080/api/v1/employees
  
- Add a new employee:

  > curl -s -u axa:12345 -H "Content-Type: application/json" -XPOST http://localhost:8080/api/v1/employees --data '{"name": "Adam Pahlevi Baihaqi", "salary": "666666", "department": "Software Engineering"}'
  
- Get an employee, assume the employee's ID is 1:

  > curl -u axa:12345 http://localhost:8080/api/v1/employees/1
  
- Update an employee, assume the ID is 1:

  > curl -s -u axa:12345 -H "Content-Type: application/json" -XPUT http://localhost:8080/api/v1/employees/1 --data '{"name": "Adam Pahlevi Baihaqi", "salary": "999999", "department": "Software Engineering"}'
  
- Delete an employee, assume the ID is 1:

  > curl -u axa:12345 -XDELETE http://localhost:8080/api/v1/employees/1
  

#### Your experience in Java

I have used Spring Boot in the past, and in my side projects. Currently, I work as Ruby on Rails engineers (and have written a book about it published by Apress). However, about Java and Spring Boot, I think I am quite confident with the tools too.