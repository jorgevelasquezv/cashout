package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.Payment;
import co.com.jorge.cashout.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {PaymentsController.class})
@WebFluxTest(PaymentsController.class)
class PaymentsControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getPayments() {
        Payment payment = new Payment();
        payment.setUserId(3L);
        payment.setAmount(BigDecimal.valueOf(100.0));

        webTestClient.post()
          .uri("/payments")
          .bodyValue(payment)
          .exchange()
          .expectStatus().isOk()
          .expectBody(String.class)
          .value(response -> {
              assertTrue(response.contains(PaymentStatus.APPROVED.name()));
          });
    }

    @Test
    void getPayments_rejected() {
        Payment payment = new Payment();
        payment.setUserId(4L);
        payment.setAmount(BigDecimal.valueOf(100.0));

        webTestClient.post()
          .uri("/payments")
          .bodyValue(payment)
          .exchange()
          .expectStatus().isOk()
          .expectBody(String.class)
          .value(response -> {
              assertTrue(response.contains(PaymentStatus.REJECTED.name()));
          });
    }

    @Test
    void getPayments_invalid() {
        Payment payment = new Payment();
        payment.setUserId(5L);
        payment.setAmount(BigDecimal.valueOf(100.0));

        webTestClient.post()
          .uri("/payments")
          .bodyValue(payment)
          .exchange()
          .expectStatus().isOk()
          .expectBody(String.class)
          .value(response -> {
              assertTrue(response.contains(PaymentStatus.INVALID.name()));
          });
    }
}