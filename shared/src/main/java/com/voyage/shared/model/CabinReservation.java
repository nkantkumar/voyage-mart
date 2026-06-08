package com.voyage.shared.model;

import java.io.Serializable;

public class CabinReservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String cruiseId;
    private String cabinId;
    private String customerId;
    private double price;
    private String status; // RESERVED, CONFIRMED, CANCELLED

    public CabinReservation() {}

    public CabinReservation(String reservationId, String cruiseId, String cabinId, String customerId, double price, String status) {
        this.reservationId = reservationId;
        this.cruiseId = cruiseId;
        this.cabinId = cabinId;
        this.customerId = customerId;
        this.price = price;
        this.status = status;
    }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public String getCruiseId() { return cruiseId; }
    public void setCruiseId(String cruiseId) { this.cruiseId = cruiseId; }

    public String getCabinId() { return cabinId; }
    public void setCabinId(String cabinId) { this.cabinId = cabinId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "CabinReservation{" +
                "reservationId='" + reservationId + '\'' +
                ", cruiseId='" + cruiseId + '\'' +
                ", cabinId='" + cabinId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
