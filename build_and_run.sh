#!/bin/bash
set -e

echo "=== Building services with Bazel ==="
bazel build //booking-service:booking-service //cruise-service:cruise-service //payment-service:payment-service

echo "=== Copying JARs to Docker build contexts ==="
rm -f booking-service/booking-service.jar cruise-service/cruise-service.jar payment-service/payment-service.jar
cp bazel-bin/booking-service/booking-service.jar booking-service/
cp bazel-bin/cruise-service/cruise-service.jar cruise-service/
cp bazel-bin/payment-service/payment-service.jar payment-service/

echo "=== Starting Docker Compose ==="
docker-compose up --build
