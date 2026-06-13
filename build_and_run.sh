#!/bin/bash
set -e

echo "=== Building services with Bazel ==="
bazel build //booking-service:booking-service //cruise-service:cruise-service //payment-service:payment-service

echo "=== Copying JARs to Docker build contexts ==="
rm -f booking-service/booking-service.jar cruise-service/cruise-service.jar payment-service/payment-service.jar
rm -f unified/*.jar
cp bazel-bin/booking-service/booking-service.jar unified/
cp bazel-bin/cruise-service/cruise-service.jar unified/
cp bazel-bin/payment-service/payment-service.jar unified/

echo "=== Starting Docker Compose ==="
docker-compose up --build
