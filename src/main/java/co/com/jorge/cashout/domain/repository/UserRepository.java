package co.com.jorge.cashout.domain.repository;

import co.com.jorge.cashout.domain.entities.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
