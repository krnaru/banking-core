FROM gradle:8.7-jdk21 as builder

WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY src /app/src

RUN gradle build -x test --no-daemon


FROM eclipse-temurin:21

WORKDIR /app

COPY --from=builder /app/build/libs/CoreBanking-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]