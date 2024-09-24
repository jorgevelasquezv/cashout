package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.CashOut;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.domain.repository.CashOutRepository;
import co.com.jorge.cashout.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionHistoryControllerFunctionalTest {

    private static final AtomicLong userId = new AtomicLong();
    private static final AtomicLong cashOutId = new AtomicLong();

    @Autowired
    private WebTestClient webTestClient;

    @AfterAll
    static void cleanUp(@Autowired UserRepository userRepository, @Autowired CashOutRepository cashOutRepository) {
        cashOutRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }

    @BeforeAll
    static void setup(@Autowired UserRepository userRepository, @Autowired CashOutRepository cashOutRepository) {
        User user = new User("Jorge", BigDecimal.valueOf(100.0));
        userRepository.save(user)
          .doOnSuccess(userDB -> userId.set(userDB.getId()))
          .block();

        CashOut cashOut = new CashOut();
        cashOut.setUserId(userId.get());
        cashOut.setAmount(BigDecimal.valueOf(10.0));
        cashOutRepository.save(cashOut)
          .doOnSuccess(cashOutDB -> cashOutId.set(cashOutDB.getId()))
          .block();
    }

    @Test
    @Order(1)
    void getTransactionHistoryByUserId() {
        webTestClient.get()
          .uri("/transaction-history/user/{userId}", userId.get())
          .exchange()
          .expectStatus().isOk()
          .expectBodyList(CashOut.class);
    }

    @Test
    @Order(2)
    void getTransactionHistoryByUserId_notFound() {
        webTestClient.get()
          .uri("/transaction-history/user/{userId}", 0)
          .exchange()
          .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void getTransactionHistoryByCashOutId() {
        webTestClient.get()
          .uri("/transaction-history/cashout/{cashOutId}", cashOutId)
          .exchange()
          .expectStatus().isOk()
          .expectBody(CashOut.class);
    }

    @Test
    @Order(4)
    void getTransactionHistoryByCashOutId_notFound() {
        webTestClient.get()
          .uri("/transaction-history/cashout/{cashOutId}", 0)
          .exchange()
          .expectStatus().isNotFound();
    }

}
