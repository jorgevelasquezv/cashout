package co.com.jorge.cashout.controller;

import co.com.jorge.cashout.domain.entities.CashOut;
import co.com.jorge.cashout.services.interfaces.ICashOutService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/cashouts")
public class CashOutController {

    private final ICashOutService cashOutService;

    public CashOutController(ICashOutService cashOutService) {
        this.cashOutService = cashOutService;
    }

    @RequestMapping("/user/{userId}")
    public Flux<CashOut> getCashOutsByUserId(@PathVariable("userId") Long userId) {
        return cashOutService.getCashOutsByUserId(userId);
    }

    @PostMapping
    public Mono<CashOut> createCashOut(@Valid @RequestBody CashOut cashOut) {
        return cashOutService.createCashOut(cashOut);
    }
}
