FROM voyage-services-base:latest
WORKDIR /app

# Copy thin JARs (small, compiled extremely fast)
COPY booking-service.jar ./booking.jar
COPY cruise-service.jar ./cruise.jar
COPY payment-service.jar ./payment.jar
COPY shared.jar ./shared.jar

COPY entrypoint.sh entrypoint.sh
EXPOSE 8081 8082 8083
ENTRYPOINT ["/app/entrypoint.sh"]
