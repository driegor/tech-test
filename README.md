## Build

```
$ mvn package
```

## Test

```
$ mvn test
```

## Run

```
$ mvn package
$ java -jar ./target/test-web-application.jar
```

## Usage

##### Data

Users
user1,user2,user3,admin

Password:123456

Roles
PAGE_1,PAGE_2,PAGE_3,ADMIN

User roles
user1->PAGE_1
user2->PAGE_1,PAGE_2
user3->PAGE_1,PAGE_3,PAGE_3
admin->ADMIN


##### Api

http://localhost:8080/api/users

This rest endpoint uses Basic authentication, these are the headers for the users above

user1 ->AUTHORIZATION:Basic dXNlcjE6MTIzNDU2
user2 ->AUTHORIZATION:Basic dXNlcjI6MTIzNDU2
user3 ->AUTHORIZATION:Basic dXNlcjM6MTIzNDU2
admin ->AUTHORIZATION:Basic YWRtaW46MTIzNDU2


##### Pages
http://localhost:8080/pages/Page 1
http://localhost:8080/pages/Page 2
http://localhost:8080/pages/Page 3
