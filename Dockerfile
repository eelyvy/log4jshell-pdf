# Start from a base alpine image
FROM maven AS build

ADD . /log4jshell-pdf
WORKDIR /log4jshell-pdf
RUN mvn clean package

FROM openjdk:8u181-jdk-alpine

RUN apk add --no-cache maven bash curl wget

RUN mkdir /app
COPY --from=build  /log4jshell-pdf/target/pdfbox-server-1.0-SNAPSHOT.jar /app/pdfbox-server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/pdfbox-server.jar"]