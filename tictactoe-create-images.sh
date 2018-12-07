#!/usr/bin/env bash
echo ./gradlew clean build -x test
./gradlew clean build -x test
echo ./gradlew :tictactoe-services:user-service:docker
./gradlew :tictactoe-services:user-service:docker
echo ./gradlew :tictactoe-services:game-service:docker
./gradlew :tictactoe-services:game-service:docker
echo ./gradlew :tictactoe-services:web-api:docker
./gradlew :tictactoe-services:web-api:docker

