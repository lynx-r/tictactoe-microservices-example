# An example of a simple microservices application

# Users

## Authenticate

![Login with Basic Auth](https://monosnap.com/file/d0ugMpb6iKuP9nvOcooCp5zCzJegYi)

## View users

`http://localhost:5555/api/user/v1/users`

![View all Users](https://monosnap.com/file/9Hf3p6prihdKGQH66jvUtI6bAhKGSo)

# Games

## Authenticate

![Login with Basic Auth](https://monosnap.com/file/uEEytZf4QcySoEJtmfOh8dKTrlUJji)

There are two users:

user / password
admin / password

User can view games. Admin can create games.

## View games

`http://localhost:5555/api/game/v1/games`

![Request all Games](https://monosnap.com/file/PpfDWVpa6QwAHTFC7Z9xaftLvTobwC)

## Create game

`http://localhost:5555/api/game/v1/games/5bff786c6d0ae45f786b622d/5bff786c6d0ae45f786b622c?black=true`

![Create a Game](https://monosnap.com/file/5GikwzspvziRf67LrLXGhTi9tQCXHL)

Where `5bff786c6d0ae45f786b622d` - the first user, `5bff786c6d0ae45f786b622c` - the second user 
and `black` marks the first like the black

    Only user authenticated as admin can create a game.

## Run Zipkin server

See this [Zipkin quickstart](https://github.com/openzipkin/zipkin#quick-start)

I run it like this:

```
docker run -d -p 9411:9411 openzipkin/zipkin
```
 
And I added this line to my `/etc/hosts`:

```
192.168.1.2     mydomain
```