FROM 518370512832.dkr.ecr.ap-northeast-2.amazonaws.com/image/openjdk11:latest as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

ENV aws_access_key_id aws_access_key_id
ENV aws_secret_access_key aws_secret_access_key

RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM 518370512832.dkr.ecr.ap-northeast-2.amazonaws.com/image/openjdk11:latest
COPY --from=builder build/libs/*.jar microservice-community-java-0.0.1-SNAPSHOT.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "./microservice-community-java-0.0.1-SNAPSHOT.jar"]