package guemri.oc_p11.medhead.destination.feignclient.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "hospital-service",
        url = "http://localhost:8090",
        path = "api/v1/notification"
)
public interface NotificationClient {

    @PutMapping("/send")
    ResponseEntity sendNotification(@RequestBody NotificationRequest notification) throws InterruptedException;
}
