package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.hudyma.service.RandomAccountService;

@RestController
@RequiredArgsConstructor
public class LoadTestController {
    private final RandomAccountService accountService;

    @PostMapping("/api/debit")
    public Mono<Void> debit() {
        return accountService.debitRandomAccount();
    }
}

