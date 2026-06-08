package com.voyage.cruise.activity;

import com.voyage.shared.activity.CruiseActivities;
import com.voyage.shared.model.CabinReservation;
import io.temporal.failure.ApplicationFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CruiseActivitiesImpl implements CruiseActivities {
    private static final Logger logger = LoggerFactory.getLogger(CruiseActivitiesImpl.class);

    private final Map<String, Boolean> cabinAvailability = new ConcurrentHashMap<>();
    private final Map<String, CabinReservation> activeReservations = new ConcurrentHashMap<>();

    public CruiseActivitiesImpl() {
        // Pre-populate some mock cabins
        cabinAvailability.put("cabin_101", true);
        cabinAvailability.put("cabin_102", true);
        cabinAvailability.put("cabin_103", true);
    }

    @Override
    public CabinReservation reserveCabin(String cruiseId, String cabinId, String customerId, double price) {
        logger.info("Attempting to reserve cabin {} for cruise {} for customer {}", cabinId, cruiseId, customerId);

        if (!cabinAvailability.containsKey(cabinId)) {
            logger.error("Cabin {} does not exist", cabinId);
            throw ApplicationFailure.newNonRetryableFailure("Cabin " + cabinId + " does not exist", "CABIN_NOT_FOUND");
        }

        synchronized (cabinAvailability) {
            if (!cabinAvailability.get(cabinId)) {
                logger.error("Cabin {} is already reserved", cabinId);
                throw ApplicationFailure.newNonRetryableFailure("Cabin " + cabinId + " is already reserved", "CABIN_ALREADY_RESERVED");
            }
            cabinAvailability.put(cabinId, false);
        }

        String reservationId = UUID.randomUUID().toString();
        CabinReservation reservation = new CabinReservation(
                reservationId, cruiseId, cabinId, customerId, price, "RESERVED"
        );
        activeReservations.put(reservationId, reservation);

        logger.info("Successfully reserved cabin. Reservation: {}", reservation);
        return reservation;
    }

    @Override
    public void releaseCabin(String reservationId) {
        logger.info("Releasing cabin for reservation ID: {}", reservationId);

        CabinReservation reservation = activeReservations.remove(reservationId);
        if (reservation == null) {
            logger.warn("Reservation {} not found. Nothing to release.", reservationId);
            return;
        }

        synchronized (cabinAvailability) {
            cabinAvailability.put(reservation.getCabinId(), true);
        }

        logger.info("Successfully released cabin {} for reservation ID {}", reservation.getCabinId(), reservationId);
    }
}
