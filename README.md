# An example of a simple microservices application

# Run in Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/3d22b6efe9ada28ae2de)

Or import `tictactoe-postman_collection.json`

Services and Credential (**login/password**):

user/user

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

    Before run `docker-compose` copy `tictactoe-shared.env` into `${HOME}/Docker/tictactoe-shared.env`.

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
