FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :AccountService:bootWar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/AccountService/build/libs/*.war app.war
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.war"]
