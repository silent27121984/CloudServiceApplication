FROM openjdk:17-alpine
LABEL authors="silent27"
EXPOSE 8099
ADD target/CloudServiceApplication-0.0.1-SNAPSHOT.jar CloudService.jar
ENTRYPOINT ["java", "-jar", "/CloudService.jar"]