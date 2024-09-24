package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.services.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public Flux<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public Mono<User> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public Mono<User> updateUserBalance(@PathVariable("id") Long id, @Valid @RequestBody Balance balance) {
        return userService.updateUserBalance(id, balance);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }
}
