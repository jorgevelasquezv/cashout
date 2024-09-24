package co.com.jorge.cashout.services.interfaces;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> createUser(User user);
    Mono<User> getUser(Long id);
    Flux<User> getUsers();
    Mono<User> updateUserBalance(Long id, Balance balance);
    Mono<Void> deleteUser(Long id);
}
