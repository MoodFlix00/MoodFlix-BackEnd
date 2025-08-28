# ------------------ 1단계: 빌드 ------------------
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /home/gradle/src

# 1. 빌드에 필요한 최소한의 파일만 먼저 복사합니다.
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# 2. gradlew에 실행 권한을 부여합니다.
RUN chmod +x ./gradlew

# 3. 소스 코드 없이 의존성만 먼저 다운로드하여 캐시를 활용합니다.
RUN ./gradlew --no-daemon dependencies

# 4. 실제 소스 코드를 복사합니다.
COPY src ./src

# 5. 애플리케이션을 빌드합니다.
RUN ./gradlew --no-daemon bootJar -x test


# ------------------ 2단계: 실행 ------------------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# (보안) 애플리케이션 실행을 위한 별도의 사용자 계정 생성
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# 빌더에서 생성된 JAR 복사 및 파일 소유권 변경
COPY --chown=appuser:appgroup --from=builder /home/gradle/src/build/libs/app.jar /app/app.jar

# 생성한 사용자로 전환
USER appuser

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
