package com.voyage.shared.activity;

import com.voyage.shared.model.CabinReservation;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CruiseActivities {

    @ActivityMethod
    CabinReservation reserveCabin(String cruiseId, String cabinId, String customerId, double price);

    @ActivityMethod
    void releaseCabin(String reservationId);
}
