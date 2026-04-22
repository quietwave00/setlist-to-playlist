# Build frontend
FROM node:20-bookworm-slim AS client-build
WORKDIR /app/client

COPY client/package*.json ./
RUN npm ci

COPY client ./
RUN npm run build


# Build backend
FROM eclipse-temurin:17-jdk-jammy AS server-build
WORKDIR /app

# Gradle
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies

# Copy
COPY src src
RUN rm -rf src/main/resources/static && mkdir -p src/main/resources/static
COPY --from=client-build /app/client/dist src/main/resources/static

# Build jar
RUN ./gradlew --no-daemon clean bootJar

# Runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=server-build /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
