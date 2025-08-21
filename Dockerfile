FROM openjdk:17
EXPOSE 9090
ADD target/sb-ecom.jar /app/sb-ecom.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "docker-ci-cd.jar"]