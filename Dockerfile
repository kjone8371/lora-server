FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY . /app
# Gradle Wrapper 사용
RUN ./gradlew clean build --no-daemon --stacktrace --info
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080