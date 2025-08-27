# ------------------ 1단계: 빌드 환경 (Builder) ------------------
# Gradle과 Java 17이 포함된 이미지를 사용하여 빌드 환경을 구성합니다.
FROM gradle:8.9-jdk17 AS builder

# 작업 디렉토리를 지정합니다.
WORKDIR /home/gradle/src

# 빌드에 필요한 파일들을 먼저 복사하여 Docker 캐시를 활용합니다.
# gradlew 파일과 gradle 폴더도 함께 복사해야 합니다.
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Gradle을 사용하여 의존성을 다운로드합니다.
# 이렇게 하면 소스 코드가 변경되어도 의존성은 다시 받지 않아 빌드 속도가 향상됩니다.
RUN ./gradlew build --no-daemon || true

# 나머지 소스 코드를 복사합니다.
COPY .github/workflows .

# 테스트를 제외하고 애플리케이션을 빌드합니다.
RUN ./gradlew build --no-daemon -x test


# ------------------ 2단계: 실행 환경 (Runner) ------------------
# 실제 애플리케이션을 실행하는 데 필요한 최소한의 환경만 사용합니다.
# JRE(Java Runtime Environment)만 포함된 가벼운 이미지를 사용합니다.
FROM eclipse-temurin:17-jre-jammy

# 작업 디렉토리를 지정합니다.
WORKDIR /app

# 1단계(builder)에서 생성된 JAR 파일을 복사해옵니다.
COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

# 애플리케이션이 8080 포트를 사용함을 명시합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 JAR 파일을 실행하는 명령어를 지정합니다.
ENTRYPOINT ["java","-jar","app.jar"]