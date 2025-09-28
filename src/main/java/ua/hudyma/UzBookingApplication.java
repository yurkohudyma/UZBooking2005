package ua.hudyma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UzBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(UzBookingApplication.class, args);
    }
}
