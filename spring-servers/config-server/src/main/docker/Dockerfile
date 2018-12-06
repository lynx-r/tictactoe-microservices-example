FROM openjdk:10
RUN mkdir -p /usr/local/configserver
ARG JAR_FILE
COPY ${JAR_FILE} /usr/local/configserver/configserver.jar
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh