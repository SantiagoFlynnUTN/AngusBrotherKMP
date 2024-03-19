FROM openjdk:11 as builder

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

RUN ./gradlew buildFatJar

FROM openjdk:11

COPY --from=builder /app/build/libs/service_chat.jar service_chat.jar

CMD ["java", "-jar", "service_chat.jar"]

EXPOSE 8081