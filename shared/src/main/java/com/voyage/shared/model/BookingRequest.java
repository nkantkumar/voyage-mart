package com.voyage.shared.model;

import java.io.Serializable;

public class BookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerId;
    private String cruiseId;
    private String cabinId;
    private double amount;

    public BookingRequest() {}

    public BookingRequest(String customerId, String cruiseId, String cabinId, double amount) {
        this.customerId = customerId;
        this.cruiseId = cruiseId;
        this.cabinId = cabinId;
        this.amount = amount;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCruiseId() { return cruiseId; }
    public void setCruiseId(String cruiseId) { this.cruiseId = cruiseId; }

    public String getCabinId() { return cabinId; }
    public void setCabinId(String cabinId) { this.cabinId = cabinId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "customerId='" + customerId + '\'' +
                ", cruiseId='" + cruiseId + '\'' +
                ", cabinId='" + cabinId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
