#--BUILD--
FROM maven:3.6.1-jdk-8-alpine as MAVEN_BUILD
MAINTAINER mr.nhut.dev@gmail.com

WORKDIR /build
COPY . .
RUN find . -type f -name 'application-my.properties' -delete
RUN find . -type f -name 'application-prod.properties' -delete
RUN mvn package

#--RELEASE--
FROM openjdk:8-jre-alpine

WORKDIR /app
COPY /src/main/resources/https-keystore.p12 /app
COPY --from=MAVEN_BUILD /build/target/simple-monitor-server-1.0.0.jar /app/

ENV spring.profiles.active=prod
EXPOSE 443
ENTRYPOINT ["java","-jar","simple-monitor-server-1.0.0.jar"]
