FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :GatewayService:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/GatewayService/build/libs/*.jar app.jar
EXPOSE 8880
ENTRYPOINT ["java","-jar","app.jar"]
