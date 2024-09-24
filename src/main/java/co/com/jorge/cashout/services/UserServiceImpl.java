package co.com.jorge.cashout.services;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.domain.repository.UserRepository;
import co.com.jorge.cashout.exceptions.NotFoundUserException;
import co.com.jorge.cashout.services.interfaces.IUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> getUser(Long id) {
        return userRepository.findById(id)
          .switchIfEmpty(Mono.error(new NotFoundUserException("User not found")));
    }

    public Flux<User> getUsers() {
        return userRepository.findAll();
    }

    public Mono<User> updateUserBalance(Long id, Balance balance) {
        return getUser(id)
          .flatMap(existingUser -> {
              existingUser.setBalance(existingUser.getBalance().add(balance.getAmount()));
              return userRepository.save(existingUser);
          });
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}
