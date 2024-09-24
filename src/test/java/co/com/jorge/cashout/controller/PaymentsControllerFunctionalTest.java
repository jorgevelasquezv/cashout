package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.Payment;
import co.com.jorge.cashout.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PaymentsControllerFunctionalTest {

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
          .expectBody(PaymentStatus.class)
          .isEqualTo(PaymentStatus.APPROVED);
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
          .expectBody(PaymentStatus.class)
          .isEqualTo(PaymentStatus.REJECTED);
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
          .expectBody(PaymentStatus.class)
          .isEqualTo(PaymentStatus.INVALID);
    }
}
