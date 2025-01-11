# OpenJDK 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드한 JAR 파일을 Docker 이미지에 복사
ARG JAR_FILE=build/libs/lora-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar


# 컨테이너 시작 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]

# 포트 8080을 열어두기
EXPOSE 8080