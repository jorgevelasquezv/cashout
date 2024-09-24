package co.com.jorge.cashout.services;

import co.com.jorge.cashout.domain.entities.Balance;
import co.com.jorge.cashout.domain.entities.User;
import co.com.jorge.cashout.domain.repository.UserRepository;
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
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServices;

    @Test
    void createUser() {
        User userInput = new User();
        userInput.setName("Jorge");
        userInput.setBalance(BigDecimal.valueOf(100.0));

        User userOutput = new User();
        userOutput.setId(1L);
        userOutput.setName("Jorge");
        userOutput.setBalance(BigDecimal.valueOf(100.0));

        Mockito.when(userRepository.save(userInput)).thenReturn(Mono.just(userOutput));

        StepVerifier.create(userServices.createUser(userInput))
          .expectNext(userOutput)
          .verifyComplete();
    }

    @Test
    void getUser() {
        User userOutput = new User();
        userOutput.setId(1L);
        userOutput.setName("Jorge");
        userOutput.setBalance(BigDecimal.valueOf(100.0));

        Mockito.when(userRepository.findById(1L)).thenReturn(Mono.just(userOutput));

        StepVerifier.create(userServices.getUser(1L))
          .expectNext(userOutput)
          .verifyComplete();
    }

    @Test
    void getUser_notFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userServices.getUser(1L))
          .verifyError();
    }

    @Test
    void getUsers() {
        User userOutputOne = new User();
        userOutputOne.setId(1L);
        userOutputOne.setName("Jorge");
        userOutputOne.setBalance(BigDecimal.valueOf(100.0));

        User userOutputTwo = new User();
        userOutputTwo.setId(2L);
        userOutputTwo.setName("Isaac");
        userOutputTwo.setBalance(BigDecimal.valueOf(150.0));

        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(userOutputOne, userOutputTwo));

        StepVerifier.create(userServices.getUsers())
          .expectNext(userOutputOne)
          .expectNext(userOutputTwo)
          .verifyComplete();
    }

    @Test
    void updateUserBalance() {
        User userOutput = new User();
        userOutput.setId(1L);
        userOutput.setName("Jorge");
        userOutput.setBalance(BigDecimal.valueOf(100.0));

        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(100.0));

        User userUpdated = new User();
        userUpdated.setId(1L);
        userUpdated.setName("Jorge");
        userUpdated.setBalance(BigDecimal.valueOf(200.0));

        Mockito.when(userRepository.findById(1L)).thenReturn(Mono.just(userOutput));
        Mockito.when(userRepository.save(userOutput)).thenReturn(Mono.just(userUpdated));

        StepVerifier.create(userServices.updateUserBalance(1L,balance))
          .expectNext(userUpdated)
          .verifyComplete();
    }

    @Test
    void deleteUser() {
        Mockito.when(userRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userServices.deleteUser(1L))
          .verifyComplete();
    }
}