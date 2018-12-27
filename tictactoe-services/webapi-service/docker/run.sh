#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $DISCOVERY_SERVER_PORT)"
echo "********************************************************"
while ! `nc -z discoveryserver  $(getPort $DISCOVERY_SERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $(getPort $CONFIG_SERVER_PORT)"
echo "********************************************************"
while ! `nc -z configserver $(getPort $CONFIG_SERVER_PORT)`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Starting WebApi Service with $CONFIG_SERVER_URI"
echo "********************************************************"
/app/app.jar
