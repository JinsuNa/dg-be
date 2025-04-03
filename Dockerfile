# OpenJDK 17을 기반으로 하는 경량화 스프링 부트 이미지
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# ✅ JAR 파일을 정확히 지정해서 복사
COPY build/libs/app.jar app.jar

# 포트 설정
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
