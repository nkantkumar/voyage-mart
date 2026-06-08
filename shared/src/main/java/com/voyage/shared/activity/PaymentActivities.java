package com.voyage.shared.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PaymentActivities {

    @ActivityMethod
    String processPayment(String customerId, String reservationId, double amount);

    @ActivityMethod
    void refundPayment(String transactionId, double amount);
}
