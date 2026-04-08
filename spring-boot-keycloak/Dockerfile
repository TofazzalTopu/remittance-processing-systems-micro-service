FROM openjdk:11-jdk-alpine
ADD target/spring-boot-keycloak.jar spring-boot-keycloak.jar
ENTRYPOINT ["java", "-jar", "spring-boot-keycloak.jar"]
EXPOSE 8081