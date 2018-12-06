FROM openjdk:11
RUN apt-get update && apt-get -y install netcat-traditional
RUN mkdir app
ARG JAR_FILE
ARG RUN_SH
COPY ${JAR_FILE} /app/app.jar
COPY ${RUN_SH} /app/run.sh
ENTRYPOINT ["sh", "/app/run.sh"]
