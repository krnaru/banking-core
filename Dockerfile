FROM eclipse-temurin:21

WORKDIR /app

COPY build/libs/CoreBanking-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app/app.jar"]
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
