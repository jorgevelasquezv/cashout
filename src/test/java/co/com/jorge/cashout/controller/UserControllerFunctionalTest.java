package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerFunctionalTest {

    private static final AtomicLong userId = new AtomicLong();

    private final User user = new User("Jorge", BigDecimal.valueOf(100.0));

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void cleanUp(@Autowired UserRepository userRepository) {
        userRepository.deleteAll().block();
    }

    @Test
    @Order(1)
    void createUser() {

        webTestClient.post()
          .uri("/users")
          .bodyValue(user)
          .exchange()
          .expectStatus().isOk()
          .expectBody(User.class)
          .value(userResponse -> {
              assertEquals(user.getName(), userResponse.getName());
              assertEquals(user.getBalance(), userResponse.getBalance());
              assertNotNull(userResponse.getId());
              userId.set(userResponse.getId());
          });
    }

    @Test
    @Order(2)
    void createUser_badRequest() {
        User user = new User();

        webTestClient.post()
          .uri("/users")
          .bodyValue(user)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody()
          .jsonPath("$.code").isEqualTo(400)
          .jsonPath("$.title").isEqualTo("BAD_REQUEST")
          .jsonPath("$.message").isEqualTo("Validation error")
          .jsonPath("$.errors").isArray();
    }


    @Test
    @Order(3)
    void updateUserBalance() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(200.0));

        BigDecimal balanceExpected = user.getBalance().add(balance.getAmount());

        webTestClient.put()
          .uri("/users/{id}", userId.get())
          .bodyValue(balance)
          .exchange()
          .expectStatus().isOk()
          .expectBody(User.class)
          .value(userResponse -> {
              assertEquals(0, userResponse.getBalance().compareTo(balanceExpected));
          });
    }

    @Test
    @Order(4)
    void updateUserBalance_badRequest() {
        Balance balance = new Balance();

        webTestClient.put()
          .uri("/users/{id}", userId.get())
          .bodyValue(balance)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody()
          .jsonPath("$.code").isEqualTo(400)
          .jsonPath("$.title").isEqualTo("BAD_REQUEST")
          .jsonPath("$.message").isEqualTo("Validation error")
          .jsonPath("$.errors").isArray();
    }

    @Test
    @Order(5)
    void getUser() {
        webTestClient.get()
          .uri("/users/{id}", userId.get())
          .exchange()
          .expectStatus().isOk()
          .expectBody(User.class)
          .value(userResponse -> {
              assertEquals(user.getName(), userResponse.getName());
              assertEquals(userId.get(), userResponse.getId());
          });
    }

    @Test
    @Order(6)
    void getUser_notFound() {
        webTestClient.get()
          .uri("/users/{id}", 100)
          .exchange()
          .expectStatus().isNotFound();
    }

    @Test
    @Order(7)
    void getUsers() {
        webTestClient.get()
          .uri("/users")
          .exchange()
          .expectStatus().isOk()
          .expectBodyList(User.class)
          .value(users -> {
              assertFalse(users.isEmpty());
          });
    }

    @Test
    @Order(8)
    void deleteUser() {
        webTestClient.delete()
          .uri("/users/{id}", userId.get())
          .exchange()
          .expectStatus().isOk();
    }
}
