package com.voyage.shared.model;

import java.io.Serializable;

public class BookingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String status; // CONFIRMED, FAILED, CANCELLED
    private String message;

    public BookingResponse() {}

    public BookingResponse(String bookingId, String status, String message) {
        this.bookingId = bookingId;
        this.status = status;
        this.message = message;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "bookingId='" + bookingId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
