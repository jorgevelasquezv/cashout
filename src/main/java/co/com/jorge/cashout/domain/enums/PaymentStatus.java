package co.com.jorge.cashout.domain.enums;

public enum PaymentStatus {
    INVALID("INVALID"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
