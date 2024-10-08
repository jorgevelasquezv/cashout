package co.com.jorge.cashout.domain.entities;

import java.math.BigDecimal;

public class Payment {
    private Long userId;
    private BigDecimal amount;

    public Payment() {
    }

    public Payment(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
