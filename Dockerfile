FROM 677284563273.dkr.ecr.ap-northeast-2.amazonaws.com/babyfood/openjdk:latest as builder
#FROM openjdk:latest as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

#ENV aws_access_key_id aws_access_key_id
#ENV aws_secret_access_key aws_secret_access_key

RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM 677284563273.dkr.ecr.ap-northeast-2.amazonaws.com/babyfood/openjdk:latest
#FROM openjdk:latest
COPY --from=builder build/libs/*.jar microservice-platform-recipe-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./microservice-platform-recipe-0.0.1-SNAPSHOT.jar"]