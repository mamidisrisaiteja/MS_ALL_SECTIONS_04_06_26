#start with a base image containing java runtime
FROM eclipse-temurin:17-jdk

LABEL maintainer="eazy-bytes.com"

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]