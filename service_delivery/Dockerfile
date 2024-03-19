FROM openjdk:11 as builder

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

RUN ./gradlew buildFatJar

FROM openjdk:11

COPY --from=builder /app/build/libs/service_delivery.jar service_delivery.jar

CMD ["java", "-jar", "service_delivery.jar"]

EXPOSE 8086