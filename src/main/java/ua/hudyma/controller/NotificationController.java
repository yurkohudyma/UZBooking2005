package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.service.NotificationService;

import java.util.Arrays;

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
    @DeleteMapping
    public ResponseEntity<String> deleteNotification (@RequestParam Long id){
        return ResponseEntity.status(notificationService.delete(id))
                .body("Notification deleted");
    }

    @DeleteMapping("/deleteFromList")
    public ResponseEntity<String> deleteIterable (@RequestBody Long[] list){
        return ResponseEntity.ok(notificationService.deleteIterable(Arrays.asList(list)));
    }

    @DeleteMapping("/deleteFromListInBatch")
    public ResponseEntity<String> deleteInBatch (@RequestBody Long[] list){
        return ResponseEntity.ok(notificationService.deleteInBatch(Arrays.asList(list)));
    }
}
