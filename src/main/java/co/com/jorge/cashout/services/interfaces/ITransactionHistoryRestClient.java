package co.com.jorge.cashout.services.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface ITransactionHistoryRestClient {
    @GetExchange("/transaction-history/user/{userId}")
    Mono<String> getTransactionHistory(@PathVariable("userId") Long userId);
}
