FROM openjdk:11
RUN apt-get install netcat-openbsd
RUN mkdir app
ARG JAR_FILE
ARG RUN_SH
COPY ${JAR_FILE} /app/app.jar
COPY ${RUN_SH} /app/run.sh
ENTRYPOINT ["sh", "/app/run.sh"]
