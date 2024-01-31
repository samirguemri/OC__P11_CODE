package guemri.oc_p11.medhead.notification.service;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public record NotificationService(
        KafkaTemplate<String, Object> kafkaTemplate
) {

    public boolean sendNotification(String topic, NotificationRequest notification) throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            Thread.sleep(1000);
            kafkaTemplate.send(topic, notification);
        }
        return true;
    }

}
