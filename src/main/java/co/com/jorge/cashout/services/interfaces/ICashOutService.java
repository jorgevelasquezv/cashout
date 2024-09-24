package co.com.jorge.cashout.services.interfaces;

import co.com.jorge.cashout.domain.entities.CashOut;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICashOutService {
    Mono<CashOut> createCashOut(CashOut cashOut);
    Mono<CashOut> getCashOut(Long id);
    Flux<CashOut> getCashOutsByUserId(Long userId);
}
