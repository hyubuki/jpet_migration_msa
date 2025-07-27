FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :OrderService:bootWar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/OrderService/build/libs/*.war app.war
EXPOSE 8084
ENTRYPOINT ["java","-jar","app.war"]
