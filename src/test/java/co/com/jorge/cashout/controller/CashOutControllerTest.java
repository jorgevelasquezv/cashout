package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.CashOut;
import co.com.jorge.cashout.exceptions.NotFoundCashOutException;
import co.com.jorge.cashout.exceptions.NotFoundUserException;
import co.com.jorge.cashout.exceptions.responses.ErrorResponse;
import co.com.jorge.cashout.controller.handler.GlobalHandlerError;
import co.com.jorge.cashout.services.interfaces.ICashOutService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;

@ContextConfiguration(classes = {
  CashOutController.class,
  ICashOutService.class,
  GlobalHandlerError.class,
})
@WebFluxTest(CashOutController.class)
class CashOutControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICashOutService cashOutService;

    @InjectMocks
    private CashOutController cashOutController;

    @Test
    void getCashOutsByUserId() {
        Long userId = 1L;

        CashOut cashOutOne = new CashOut();
        cashOutOne.setId(1L);
        cashOutOne.setUserId(userId);
        cashOutOne.setAmount(BigDecimal.valueOf(100.0));

        CashOut cashOutTwo = new CashOut();
        cashOutTwo.setId(2L);
        cashOutTwo.setUserId(userId);
        cashOutTwo.setAmount(BigDecimal.valueOf(200.0));

        Mockito.when(cashOutService.getCashOutsByUserId(userId)).thenReturn(Flux.just(cashOutOne, cashOutTwo));

        webTestClient.get()
          .uri("/cashouts/user/{userId}", userId)
          .exchange()
          .expectStatus().isOk()
          .expectBodyList(CashOut.class)
          .value(cashOuts -> {
              cashOuts.forEach(cashOut -> {
                  assertEquals(userId, cashOut.getUserId());
              });
          })
        ;
    }

    @Test
    void getCashOutsByUserId_notFound() {
        Long userId = 1L;

        Mockito.when(cashOutService.getCashOutsByUserId(userId))
          .thenReturn(Flux.error(new NotFoundCashOutException("CashOut not found")));

        webTestClient.get()
          .uri("/cashouts/user/{userId}", userId)
          .exchange()
          .expectStatus().isNotFound()
          .expectBody(ErrorResponse.class)
          .value(errorResponse -> {
              assertEquals("CashOut not found", errorResponse.getMessage());
          })
        ;
    }

    @Test
    void createCashOut() {
        CashOut cashOut = new CashOut();
        cashOut.setUserId(1L);
        cashOut.setAmount(BigDecimal.valueOf(100.0));

        Mockito.when(cashOutService.createCashOut(refEq(cashOut))).thenReturn(Mono.just(cashOut));

        webTestClient.post()
          .uri("/cashouts")
          .bodyValue(cashOut)
          .exchange()
          .expectStatus().isOk()
          .expectBody(CashOut.class)
          .value(cashOutResponse -> {
              assertEquals(cashOut.getUserId(), cashOutResponse.getUserId());
              assertEquals(cashOut.getAmount(), cashOutResponse.getAmount());
          })
        ;
    }

    @Test
    void createCashOut_insufficientBalance(){
        CashOut cashOut = new CashOut();
        cashOut.setUserId(1L);
        cashOut.setAmount(BigDecimal.valueOf(100.0));

        Mockito.when(cashOutService.createCashOut(refEq(cashOut)))
          .thenReturn(Mono.error(new IllegalArgumentException("Insufficient balance")));

        webTestClient.post()
          .uri("/cashouts")
          .bodyValue(cashOut)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody(ErrorResponse.class)
          .value(errorResponse -> {
              assertEquals("Insufficient balance", errorResponse.getMessage());
          })
        ;
    }

    @Test
    void createCashOut_PaymentNoApproved() {
        CashOut cashOut = new CashOut();
        cashOut.setUserId(1L);
        cashOut.setAmount(BigDecimal.valueOf(100.0));

        Mockito.when(cashOutService.createCashOut(refEq(cashOut)))
          .thenReturn(Mono.error(new IllegalArgumentException("Payment not approved")));

        webTestClient.post()
          .uri("/cashouts")
          .bodyValue(cashOut)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody(ErrorResponse.class)
          .value(errorResponse -> {
              assertEquals("Payment not approved", errorResponse.getMessage());
          })
        ;
    }

    @Test
    void createCashOut_notFound(){
        CashOut cashOut = new CashOut();
        cashOut.setUserId(0L);
        cashOut.setAmount(BigDecimal.valueOf(100.0));

        Mockito.when(cashOutService.createCashOut(refEq(cashOut)))
          .thenReturn(Mono.error(new NotFoundUserException("User not found")));

        webTestClient.post()
          .uri("/cashouts")
          .bodyValue(cashOut)
          .exchange()
          .expectStatus().isNotFound()
          .expectBody(ErrorResponse.class)
          .value(errorResponse -> {
              assertEquals("User not found", errorResponse.getMessage());
          })
        ;
    }
}