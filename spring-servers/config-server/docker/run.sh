#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $DISCOVERY_SERVER_PORT)"
echo "********************************************************"
while ! `nc -z discoveryserver  $(getPort DISCOVERY_SERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Starting Config Server"
echo "********************************************************"
/app/app.jar
