FROM maven AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src
RUN mvn package

#RUN stage
FROM openjdk:17-alpine
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/GrpcClientApp/GCApp.jar
ENTRYPOINT ["java", "-jar", "/opt/GrpcClientApp/GCApp.jar"]