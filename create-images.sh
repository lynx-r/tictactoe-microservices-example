#!/usr/bin/env bash
./gradlew clean build -x test
./gradlew :spring-servers:discovery-server:docker
./gradlew :spring-servers:config-server:docker
./gradlew :spring-servers:gateway-server:docker
./gradlew :spring-servers:admin-server:docker
