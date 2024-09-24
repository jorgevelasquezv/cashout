package co.com.jorge.cashout.domain.entities;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class Balance {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    public Balance() {
    }

    public Balance(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
