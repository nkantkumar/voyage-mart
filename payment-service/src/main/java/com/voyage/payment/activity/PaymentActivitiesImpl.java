package com.voyage.payment.activity;

import com.voyage.shared.activity.PaymentActivities;
import io.temporal.failure.ApplicationFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentActivitiesImpl implements PaymentActivities {
    private static final Logger logger = LoggerFactory.getLogger(PaymentActivitiesImpl.class);

    private final Map<String, String> processedTransactions = new ConcurrentHashMap<>();

    @Override
    public String processPayment(String customerId, String reservationId, double amount) {
        logger.info("Processing payment of ${} for reservation {} (customer: {})", amount, reservationId, customerId);

        if (amount <= 0.0) {
            logger.error("Payment failed for customer {}: Invalid amount ${}", customerId, amount);
            throw ApplicationFailure.newNonRetryableFailure(
                    "Payment processing failed: amount must be greater than zero", "INVALID_AMOUNT"
            );
        }

        String transactionId = "txn_" + UUID.randomUUID().toString().substring(0, 8);
        processedTransactions.put(transactionId, reservationId);

        logger.info("Successfully processed payment. Transaction ID: {}", transactionId);
        return transactionId;
    }

    @Override
    public void refundPayment(String transactionId, double amount) {
        logger.info("Processing refund for transaction ID: {} of amount ${}", transactionId, amount);

        String reservationId = processedTransactions.remove(transactionId);
        if (reservationId == null) {
            logger.warn("Transaction ID {} not found. Nothing to refund.", transactionId);
            return;
        }

        logger.info("Successfully refunded payment for reservation {}. Transaction {} marked as refunded.",
                reservationId, transactionId);
    }
}
