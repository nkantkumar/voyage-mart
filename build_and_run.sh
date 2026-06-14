#!/bin/bash
set -e

# Check if the base image voyage-services-base exists
if ! docker image inspect voyage-services-base:latest >/dev/null 2>&1; then
    echo "=== Base image voyage-services-base:latest not found. Building it first... ==="
    # Build fat JARs to extract dependencies
    bazel build //booking-service:booking-service //cruise-service:cruise-service //payment-service:payment-service
    
    # Copy fat JARs to unified folder
    rm -f unified/*.jar
    cp bazel-bin/booking-service/booking-service.jar unified/
    cp bazel-bin/cruise-service/cruise-service.jar unified/
    cp bazel-bin/payment-service/payment-service.jar unified/
    
    # Build the base image
    docker build -t voyage-services-base:latest -f unified/Dockerfile.base unified/
fi

echo "=== Building thin libraries with Bazel ==="
bazel build //booking-service:booking_service_lib //cruise-service:cruise_service_lib //payment-service:payment_service_lib //shared:shared

echo "=== Copying thin JARs to Docker context ==="
rm -f unified/*.jar
cp bazel-bin/booking-service/libbooking_service_lib.jar unified/booking-service.jar
cp bazel-bin/cruise-service/libcruise_service_lib.jar unified/cruise-service.jar
cp bazel-bin/payment-service/libpayment_service_lib.jar unified/payment-service.jar
cp bazel-bin/shared/libshared.jar unified/shared.jar

echo "=== Starting Docker Compose ==="
docker-compose up --build
