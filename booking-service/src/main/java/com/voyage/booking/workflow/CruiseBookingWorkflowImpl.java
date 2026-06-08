package com.voyage.booking.workflow;

import com.voyage.shared.activity.CruiseActivities;
import com.voyage.shared.activity.PaymentActivities;
import com.voyage.shared.model.BookingRequest;
import com.voyage.shared.model.BookingResponse;
import com.voyage.shared.model.CabinReservation;
import com.voyage.shared.workflow.CruiseBookingWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;

public class CruiseBookingWorkflowImpl implements CruiseBookingWorkflow {
    private static final Logger logger = Workflow.getLogger(CruiseBookingWorkflowImpl.class);

    private final CruiseActivities cruiseActivities = Workflow.newActivityStub(
            CruiseActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setTaskQueue("CruiseQueue")
                    .build()
    );

    private final PaymentActivities paymentActivities = Workflow.newActivityStub(
            PaymentActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setTaskQueue("PaymentQueue")
                    .build()
    );

    @Override
    public BookingResponse bookCruise(BookingRequest request) {
        logger.info("Starting cruise booking workflow for customer: {}", request.getCustomerId());

        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(false).build();
        Saga saga = new Saga(sagaOptions);

        CabinReservation reservation = null;
        try {
            // Step 1: Reserve cabin
            reservation = cruiseActivities.reserveCabin(
                    request.getCruiseId(),
                    request.getCabinId(),
                    request.getCustomerId(),
                    request.getAmount()
            );
            
            // Add compensation to release cabin if downstream steps fail
            final String resId = reservation.getReservationId();
            saga.addCompensation(cruiseActivities::releaseCabin, resId);

            // Step 2: Charge customer
            String transactionId = paymentActivities.processPayment(
                    request.getCustomerId(),
                    reservation.getReservationId(),
                    request.getAmount()
            );
            
            // Success
            logger.info("Cruise booking workflow completed successfully for reservation {}", reservation.getReservationId());
            return new BookingResponse(reservation.getReservationId(), "CONFIRMED", "Booking confirmed. Txn ID: " + transactionId);

        } catch (ActivityFailure e) {
            logger.error("Booking workflow failed due to activity failure. Triggering compensations...", e);
            saga.compensate();
            
            String reservationId = (reservation != null) ? reservation.getReservationId() : "N/A";
            
            // Safely get root cause message
            String causeMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            return new BookingResponse(
                    reservationId, 
                    "FAILED", 
                    "Booking failed: " + causeMessage
            );
        } catch (Exception e) {
            logger.error("Unexpected error in booking workflow: {}", e.getMessage());
            saga.compensate();
            return new BookingResponse("N/A", "FAILED", "Unexpected error: " + e.getMessage());
        }
    }
}
