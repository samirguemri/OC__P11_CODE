package guemri.oc_p11.medhead.destination.feignclient.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification-client",
        url = "${spring.cloud.openfeign.client.config.notificationClient.url}",
        path = "api/v1/notification"
)
public interface NotificationClient {

    @PostMapping("/send")
    ResponseEntity<String> sendNotification(@RequestBody NotificationRequest notification) throws InterruptedException;
}
