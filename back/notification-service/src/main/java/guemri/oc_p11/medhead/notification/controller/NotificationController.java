package guemri.oc_p11.medhead.notification.controller;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import guemri.oc_p11.medhead.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/notification")
public record NotificationController(
        NotificationService notificationService
) {

    public static final String TOPIC = "hospitalReservation";

    @PutMapping("/send")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest notification) throws InterruptedException {
        notificationService.sendNotification(TOPIC, notification);
        return new ResponseEntity<>(Map.of("message", "notification sent"), HttpStatus.OK);
    }
}
