package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Log4j2
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<String> addNotification (@RequestParam Long passengerId){
        return ResponseEntity
                .status(notificationService
                        .addNotification(passengerId)).body("Notification added");
    }

}
