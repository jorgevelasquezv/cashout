package co.com.jorge.cashout.domain.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("users")
public class User {

    @Id
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Balance is required")
    private BigDecimal balance;

    public User() {
    }

    public User(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public User(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
