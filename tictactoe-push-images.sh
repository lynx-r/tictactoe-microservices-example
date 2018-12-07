#!/usr/bin/env bash
echo ./gradlew :tictactoe-services:user-service:dockerPush
./gradlew :tictactoe-services:user-service:dockerPush
echo ./gradlew :tictactoe-services:game-service:dockerPush
./gradlew :tictactoe-services:game-service:dockerPush
echo ./gradlew :tictactoe-services:web-api:dockerPush
./gradlew :tictactoe-services:web-api:dockerPush

