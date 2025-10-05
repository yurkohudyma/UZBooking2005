package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.hudyma.repository.PassengerReactiveRepository;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RandomAccountService {

    private final PassengerReactiveRepository passengerReactiveRepository;
    private final TransactionalOperator txOperator;
    private final WebClient webClient;

    public Mono<Void> debitRandomAccount() {
        long accountId = ThreadLocalRandom.current().nextLong(1004, 37_835); // ID від 1 до 37_835
        BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(5, 20));

        return txOperator.execute(status ->
                passengerReactiveRepository.findById(accountId)
                        .flatMap(account -> {
                            if (account.getBalance().compareTo(amount) < 0) {
                                return Mono.empty(); // не оновлюємо
                            }

                            // ⚠️ Симулюємо I/O поза транзакцією (реактивно!)
                            return simulateExternalCall()
                                    .then(Mono.defer(() -> {
                                        account.setBalance(account.getBalance().subtract(amount));
                                        return passengerReactiveRepository.save(account);
                                    }));
                        })
        ).then();
    }

    private Mono<Void> simulateExternalCall() {
        /*return webClient.get()
                .uri("http://localhost:9999/delay/100") // заглушка-сервіс із затримкою 100мс
                .retrieve()
                .bodyToMono(Void.class);*/
        return WebClient.create("https://httpbin.org")
                .get()
                .uri("/delay/2") // затримка 2 секунди
                .retrieve()
                .bodyToMono(Void.class);
    }
}
