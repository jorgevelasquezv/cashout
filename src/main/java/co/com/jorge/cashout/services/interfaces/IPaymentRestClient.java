package co.com.jorge.cashout.services.interfaces;

import co.com.jorge.cashout.domain.enums.PaymentStatus;
import co.com.jorge.cashout.domain.entities.Payment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IPaymentRestClient {
    @PostExchange("/payments")
    Mono<PaymentStatus> getPayments(@RequestBody Payment payment);
}
