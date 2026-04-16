package com.example.BBA.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    public enum PaymentStatus { PENDING, SUCCESS, FAILED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "transaction_id")
    private String transactionId;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Payment() {}
    public Payment(Long id, Long bookingId, Long userId, String transactionId, PaymentStatus paymentStatus, String paymentMethod, BigDecimal amount, LocalDateTime createdAt) { this.id = id; this.bookingId = bookingId; this.userId = userId; this.transactionId = transactionId; this.paymentStatus = paymentStatus; this.paymentMethod = paymentMethod; this.amount = amount; this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static PaymentBuilder builder() { return new PaymentBuilder(); }
    public static class PaymentBuilder {
        private Long bookingId, userId;
        private String transactionId, paymentMethod;
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;
        private BigDecimal amount;
        public PaymentBuilder bookingId(Long v) { this.bookingId = v; return this; }
        public PaymentBuilder userId(Long v) { this.userId = v; return this; }
        public PaymentBuilder transactionId(String v) { this.transactionId = v; return this; }
        public PaymentBuilder paymentStatus(PaymentStatus v) { this.paymentStatus = v; return this; }
        public PaymentBuilder paymentMethod(String v) { this.paymentMethod = v; return this; }
        public PaymentBuilder amount(BigDecimal v) { this.amount = v; return this; }
        public Payment build() { return new Payment(null, bookingId, userId, transactionId, paymentStatus, paymentMethod, amount, null); }
    }
}
