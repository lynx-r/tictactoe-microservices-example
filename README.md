# An example of a simple microservices application

## GET users

`http://localhost:40001/v1/users`

## GET games

`http://localhost:40002/v1/games`

## POST game

`http://localhost:40002/v1/games/5bff786c6d0ae45f786b622d/5bff786c6d0ae45f786b622c?black=true`

Where `5bff786c6d0ae45f786b622d` - the first user, `5bff786c6d0ae45f786b622c` - the second user 
and `black` marks the first like the black

## Run Zipkin server

See this [Zipkin quickstart](https://github.com/openzipkin/zipkin#quick-start)
 