package ua.hudyma.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.hudyma.service.RandomAccountService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class LoadTestRunner implements CommandLineRunner {

    private final RandomAccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        int totalRequests = 100;
        int concurrency = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                accountService.debitRandomAccount()
                        .doOnSuccess(r -> success.incrementAndGet())
                        .doOnError(e -> {
                            failure.incrementAndGet();
                            e.printStackTrace();
                        })
                        .doFinally(sig -> latch.countDown())
                        .subscribe();
            });
        }

        latch.await();
        long totalTime = System.currentTimeMillis() - startTime;

        executor.shutdown();

        System.out.println("======== Load Test Result ========");
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Success: " + success.get());
        System.out.println("Failed: " + failure.get());
        System.out.println("Time Taken: " + totalTime + " ms");
        System.out.println("Throughput: " + (1000L * totalRequests / totalTime) + " req/sec");
    }
}
