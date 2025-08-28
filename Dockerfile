# ------------------ 1단계: 빌드 ------------------
FROM gradle:8.9-jdk21 AS builder
WORKDIR /home/gradle/src

# Gradle Wrapper 복사 및 실행 권한 부여
COPY gradlew .
COPY gradle/wrapper ./gradle/wrapper
RUN chmod +x gradlew

# 빌드 스크립트만 먼저 복사해서 의존성 캐시
COPY settings.gradle settings.gradle.kts build.gradle build.gradle.kts gradle.properties ./
COPY gradle/wrapper ./gradle/wrapper
COPY gradlew ./
RUN chmod +x ./gradlew
# 의존성만 미리 당겨 Docker 레이어 캐시 극대화
RUN ./gradlew --no-daemon dependencies || true

# 실제 소스 복사
COPY src ./src

# 테스트 제외 빌드 (bootJar 생성)
RUN ./gradlew --no-daemon clean bootJar -x test


# ------------------ 2단계: 실행 ------------------
# 실행용 JRE는 JDK와 같은 메이저 버전(21)로 맞춘다
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 빌더에서 생성된 JAR 복사
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
