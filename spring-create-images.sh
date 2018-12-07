#!/usr/bin/env bash
echo ./gradlew clean build -x test
./gradlew clean build -x test
echo ./gradlew :spring-servers:discovery-server:docker
./gradlew :spring-servers:discovery-server:docker
echo ./gradlew :spring-servers:config-server:docker
./gradlew :spring-servers:config-server:docker
echo ./gradlew :spring-servers:gateway-server:docker
./gradlew :spring-servers:gateway-server:docker
echo ./gradlew :spring-servers:admin-server:docker
./gradlew :spring-servers:admin-server:docker

