package co.com.jorge.cashout.domain.repository;

import co.com.jorge.cashout.domain.entities.CashOut;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CashOutRepository extends ReactiveCrudRepository<CashOut, Long> {
    @Query("SELECT * FROM cash_out WHERE user_id = :userId")
    Flux<CashOut> findByUserId(Long userId);
}
