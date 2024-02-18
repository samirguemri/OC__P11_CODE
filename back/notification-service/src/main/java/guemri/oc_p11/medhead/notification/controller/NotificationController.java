package guemri.oc_p11.medhead.notification.controller;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import guemri.oc_p11.medhead.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notification")
public record NotificationController(
        NotificationService notificationService
) {

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest notification) throws InterruptedException {
        notificationService.sendNotification(notification);
        return ResponseEntity.status(HttpStatus.OK).body("Notification sent");
    }
}
