FROM gradle:8.4-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :CatalogService:bootWar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/CatalogService/build/libs/*.war app.war
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.war"]
