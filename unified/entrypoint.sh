#!/bin/sh

echo "Starting all services..."

# Start booking-service
java -jar /app/booking-service.jar &
booking_pid=$!

# Start cruise-service
java -jar /app/cruise-service.jar &
cruise_pid=$!

# Start payment-service
java -jar /app/payment-service.jar &
payment_pid=$!

cleanup() {
    echo "Stopping all services..."
    kill -TERM "$booking_pid" "$cruise_pid" "$payment_pid" 2>/dev/null
    wait "$booking_pid"
    wait "$cruise_pid"
    wait "$payment_pid"
    exit 0
}

trap cleanup INT TERM

# Periodically check if any process has exited. If so, shut down everything and exit with error.
while true; do
    if ! kill -0 "$booking_pid" 2>/dev/null; then
        echo "booking-service has stopped."
        cleanup
    fi
    if ! kill -0 "$cruise_pid" 2>/dev/null; then
        echo "cruise-service has stopped."
        cleanup
    fi
    if ! kill -0 "$payment_pid" 2>/dev/null; then
        echo "payment-service has stopped."
        cleanup
    fi
    sleep 2
done
