package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.Payment;
import co.com.jorge.cashout.domain.enums.PaymentStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @PostMapping()
    public PaymentStatus getPayments(@RequestBody Payment payment) {
        Long userId = payment.getUserId();
        if (userId % 3 == 0) {
            return PaymentStatus.APPROVED;
        } else if (userId % 3 == 1) {
            return PaymentStatus.REJECTED;
        }
        return PaymentStatus.INVALID;
    }
}
