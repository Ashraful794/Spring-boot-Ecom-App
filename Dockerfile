FROM openjdk:17
EXPOSE 9090
ADD target/docker-ci-cd.jar /app/docker-ci-cd.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "docker-ci-cd.jar"]