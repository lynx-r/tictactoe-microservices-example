# An example of a simple microservices application

# Run in Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3d22b6efe9ada28ae2de#?env%5Btictactoe-local%5D=W3sia2V5IjoiSE9TVCIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo1NTU1IiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6IlRPS0VOIiwidmFsdWUiOiJleUpoYkdjaU9pSklVekkxTmlKOS5leUp6ZFdJaU9pSmhaRzFwYmlJc0ltRjFkR2h6SWpvaVVrOU1SVjlCUkUxSlRpeFNUMHhGWDFWVFJWSWlMQ0pwYzNNaU9pSjBhV04wWVdOMGIyVXVZMjl0SWl3aVpYaHdJam94TlRRME1qUXpOREUzZlEuLXF5cWptYlVQN3lTYkZiMGVsUnBxaU0xSmpsUnVUVWRaLXU5MzlkanNmUSIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJIT1NUX0NPTkZJRyIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo4ODg4IiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfV0=)

Or import `tictactoe-postman_collection.json`

Services and Credential (**login/password**):

admin/admin

actuator/actuator

# Involved Spring Cloud and other services

[Service discovery Eureka](http://localhost:8761)
eureka/password

[Cloud Config](http://localhost:8888/webapi/default)
configuser/123

[Spring Boot Admin](http://localhost:9999/#/applications)
admin/adminpassword

[Zipkin](http://localhost:9411/zipkin/)
zipkin/zipkin

# Run and scripts

Run `docker-compose` via `gradle plugin`:

    Before running `docker-compose` copy `tictactoe-shared.env` in `${HOME}/Docker/tictactoe-shared.env`.

```
./docker-compose-up.sh
```

Create images via `gradle plugin`:

```
./spring-create-images.sh
./tictactoe-create-images.sh
```

Push images via `gradle plugin`:

```
./spring-push-images.sh
./tictactoe-push-images.sh
```

# Spring Cloud Config

I use [this](https://github.com/lynx-r/tictactoe-config-repo) git repository to config application

# Links

* [Spring Microservices in Action](https://www.manning.com/books/spring-microservices-in-action)
* [Hands-On Spring 5 Security for Reactive Applications](https://www.packtpub.com/application-development/hands-spring-security-5-reactive-applications)
* [The code for the book Spring Microservices in Action](https://github.com/carnellj?tab=repositories)
* [The code for the book Hands-On Spring 5 Security for Reactive Applications](https://github.com/lynx-r/Hands-On-Spring-Security-5-for-Reactive-Applications)
