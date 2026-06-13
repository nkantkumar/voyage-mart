FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY booking-service.jar booking-service.jar
COPY cruise-service.jar cruise-service.jar
COPY payment-service.jar payment-service.jar
COPY entrypoint.sh entrypoint.sh
EXPOSE 8081 8082 8083
ENTRYPOINT ["/app/entrypoint.sh"]
