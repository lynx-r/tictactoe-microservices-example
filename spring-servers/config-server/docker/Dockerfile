FROM openjdk:jdk-oracle
RUN yum -y upgrade && yum -y install nmap-ncat
RUN mkdir app
ARG JAR_FILE
ARG RUN_SH
COPY ${JAR_FILE} /app/app.jar
COPY ${RUN_SH} /app/run.sh
ENTRYPOINT ["sh", "/app/run.sh"]
