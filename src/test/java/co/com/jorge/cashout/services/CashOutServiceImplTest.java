package co.com.jorge.cashout.services;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.CashOut;
import co.com.jorge.cashout.domain.entities.Payment;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.domain.enums.PaymentStatus;
import co.com.jorge.cashout.domain.repository.CashOutRepository;
import co.com.jorge.cashout.exceptions.NotFoundUserException;
import co.com.jorge.cashout.services.interfaces.IPaymentRestClient;
import co.com.jorge.cashout.services.interfaces.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class CashOutServiceImplTest {

    @Mock
    private IUserService userService;

    @Mock
    private CashOutRepository cashOutRepository;

    @Mock
    private IPaymentRestClient paymentRestClient;

    @InjectMocks
    private CashOutServiceImpl cashOutService;

    @Test
    void createCashOut() {
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(100.0));
        user.setName("Jorge");

        CashOut cashOut = new CashOut();
        cashOut.setUserId(user.getId());
        cashOut.setAmount(BigDecimal.valueOf(-50.0));

        Mockito.when(userService.getUser(1L)).thenReturn(Mono.just(user));
        Mockito.when(userService.updateUserBalance(Mockito.eq(user.getId()), Mockito.any(Balance.class))).thenReturn(Mono.just(user));
        Mockito.when(paymentRestClient.getPayments(Mockito.any(Payment.class))).thenReturn(Mono.just(PaymentStatus.APPROVED));
        Mockito.when(cashOutRepository.save(cashOut)).thenReturn(Mono.just(cashOut));

        StepVerifier.create(cashOutService.createCashOut(cashOut))
          .expectNext(cashOut)
          .verifyComplete();
    }

    @Test
    void createCashOut_insufficientBalance() {
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(100.0));
        user.setName("Jorge");

        CashOut cashOut = new CashOut();
        cashOut.setUserId(user.getId());
        cashOut.setAmount(BigDecimal.valueOf(-500.0));

        Mockito.when(userService.getUser(1L)).thenReturn(Mono.just(user));

        StepVerifier.create(cashOutService.createCashOut(cashOut))
          .expectError()
          .verify();
    }

    @Test
    void createCashOut_PaymentNoApproved() {
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.valueOf(100.0));
        user.setName("Jorge");

        CashOut cashOut = new CashOut();
        cashOut.setUserId(user.getId());
        cashOut.setAmount(BigDecimal.valueOf(-50.0));

        Mockito.when(userService.getUser(1L)).thenReturn(Mono.just(user));
        Mockito.when(paymentRestClient.getPayments(Mockito.any(Payment.class)))
          .thenReturn(Mono.just(PaymentStatus.REJECTED));

        StepVerifier.create(cashOutService.createCashOut(cashOut))
          .expectError()
          .verify();
    }

    @Test
    void createCashOut_notFound(){
        CashOut cashOut = new CashOut();
        cashOut.setUserId(1L);
        cashOut.setAmount(BigDecimal.valueOf(100.0));

        Mockito.when(userService.getUser(1L))
          .thenReturn(Mono.error(new NotFoundUserException("User not found")));

        StepVerifier.create(cashOutService.createCashOut(cashOut))
          .expectError()
          .verify();
    }

    @Test
    void getCashOut() {
        CashOut cashOut = new CashOut();
        cashOut.setId(1L);
        cashOut.setUserId(1L);
        cashOut.setAmount(BigDecimal.valueOf(500.0));

        Mockito.when(cashOutRepository.findById(1L)).thenReturn(Mono.just(cashOut));

        StepVerifier.create(cashOutService.getCashOut(1L))
          .expectNext(cashOut)
          .verifyComplete();
    }

    @Test
    void getCashOut_notFound() {
        Mockito.when(cashOutRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(cashOutService.getCashOut(1L))
          .expectError()
          .verify();
    }

    @Test
    void getCashOutsByUserId() {
        CashOut cashOutOne = new CashOut();
        cashOutOne.setId(1L);
        cashOutOne.setUserId(1L);
        cashOutOne.setAmount(BigDecimal.valueOf(50.0));

        CashOut cashOutTwo = new CashOut();
        cashOutTwo.setId(2L);
        cashOutTwo.setUserId(1L);
        cashOutTwo.setAmount(BigDecimal.valueOf(-100.0));


        Mockito.when(cashOutRepository.findByUserId(1L)).thenReturn(Flux.just(cashOutOne, cashOutTwo));

        StepVerifier.create(cashOutService.getCashOutsByUserId(1L))
          .expectNext(cashOutOne)
          .expectNext(cashOutTwo)
          .verifyComplete();
    }

    @Test
    void getCashOutsByUserId_notFound() {
        Mockito.when(cashOutRepository.findByUserId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(cashOutService.getCashOutsByUserId(1L))
          .expectError()
          .verify();
    }
}