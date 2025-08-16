FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :DiscoveryServer:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/DiscoveryServer/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]
