FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :CartService:bootWar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/CartService/build/libs/*.war app.war
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.war"]
