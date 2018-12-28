# An example of a simple microservices application

# Run in Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3d22b6efe9ada28ae2de#?env%5Bshashki%20local%5D=W3sia2V5IjoiSE9TVCIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo1NTU1IiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6IlRPS0VOIiwidmFsdWUiOiJleUpoYkdjaU9pSklVekkxTmlKOS5leUp6ZFdJaU9pSmhaRzFwYmlJc0ltRjFkR2h6SWpvaVVrOU1SVjlCUkUxSlRpeFNUMHhGWDFWVFJWSWlMQ0pwYzNNaU9pSjBhV04wWVdOMGIyVXVZMjl0SWl3aVpYaHdJam94TlRRME9EUTNNVGN5ZlEuQzhVRjgwaDBLQk1DVFdrRExpbVh2aDhlOHFBVjBja2Z1aFUyM05ULWY5TSIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJBTk9OX1RPS0VOIiwidmFsdWUiOiIiLCJkZXNjcmlwdGlvbiI6IiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoiSE9TVF9BUlRJQ0xFIiwidmFsdWUiOiIiLCJkZXNjcmlwdGlvbiI6IiIsInR5cGUiOiJ0ZXh0IiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJIT1NUX0JPQVJEIiwidmFsdWUiOiIiLCJkZXNjcmlwdGlvbiI6IiIsInR5cGUiOiJ0ZXh0IiwiZW5hYmxlZCI6dHJ1ZX1d)

Or import `tictactoe-postman_collection.json`

Services and Credential (**login/password**):

admin/admin

actuator/actuator

# Involved Spring Cloud and other services

[Service discovery Eureka](http://eurekatictactoe.shashki.online)
eureka/password

[Cloud Config](http://configtictactoe.shashki.online/webapi/default)
configuser/123

[Spring Boot Admin](http://admintictactoe.shashki.online/#/applications)
admin/adminpassword

[Zipkin](http://zipkintictactoe.shashki.online/zipkin/)
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
