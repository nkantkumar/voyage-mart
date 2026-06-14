#!/bin/sh

echo "Starting all services..."

# Start booking-service
java -cp "/app/booking.jar:/app/shared.jar:/app/common/lib/*" com.voyage.booking.BookingApplication &
booking_pid=$!

# Start cruise-service
java -cp "/app/cruise.jar:/app/shared.jar:/app/common/lib/*" com.voyage.cruise.CruiseApplication &
cruise_pid=$!

# Start payment-service
java -cp "/app/payment.jar:/app/shared.jar:/app/common/lib/*" com.voyage.payment.PaymentApplication &
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
