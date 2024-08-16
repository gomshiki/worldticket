# 베이스 이미지 설정
FROM amazoncorretto:21-alpine as build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 설정 파일을 복사
COPY gradlew /app/gradlew
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle/

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (빌드 캐시를 활용하기 위해 이 단계를 별도로 실행)
RUN ./gradlew dependencies

# 소스 파일 복사
COPY src /app/src

# 빌드 실행
RUN ./gradlew build

# 실제 실행 이미지 생성
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
