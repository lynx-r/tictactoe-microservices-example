#!/usr/bin/env bash
echo ./gradlew :spring-servers:discovery-server:dockerPush
./gradlew :spring-servers:discovery-server:dockerPush
echo ./gradlew :spring-servers:config-server:dockerPush
./gradlew :spring-servers:config-server:dockerPush
echo ./gradlew :spring-servers:gateway-server:dockerPush
./gradlew :spring-servers:gateway-server:dockerPush
echo ./gradlew :spring-servers:admin-server:dockerPush
./gradlew :spring-servers:admin-server:dockerPush

