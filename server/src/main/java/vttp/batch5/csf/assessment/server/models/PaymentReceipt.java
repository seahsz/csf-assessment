package vttp.batch5.csf.assessment.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class PaymentReceipt {

    private String orderId;
    private String paymentId;
    private double total;
    private long timestamp; // From payment service

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Constructor
    public PaymentReceipt(String orderId, String paymentId, double total, long timestamp) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.total = total;
        this.timestamp = timestamp;
    }

    // Payment Receipt toJson
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("orderId", orderId)
            .add("paymentId", paymentId)
            .add("total", total)
            .add("timestamp", timestamp)
            .build();
    }
    
    
}
