FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /build

RUN apk add --no-cache tzdata
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY gradlew gradlew.bat ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

COPY src src

RUN chmod +x ./gradlew && \
    ./gradlew clean build -Dquarkus.package.type=fast-jar -x test --no-daemon

FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /work/
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus

COPY --from=builder /build/build/quarkus-app/lib/ /work/lib/
COPY --from=builder /build/build/quarkus-app/app/ /work/app/
COPY --from=builder /build/build/quarkus-app/quarkus/ /work/quarkus/
COPY --from=builder /build/build/quarkus-app/quarkus-run.jar /work/quarkus-run.jar

RUN chown -R quarkus:quarkus /work
USER quarkus

ENV QUARKUS_HTTP_HOST=0.0.0.0
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/work/quarkus-run.jar"]