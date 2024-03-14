package guemri.oc_p11.medhead.notification.service;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.springframework.kafka.listener.ConsumerAwareRebalanceListener.LOGGER;

@Service
@Slf4j
public record NotificationService(
        KafkaTemplate<String, Object> kafkaTemplate
) {

    public static final String TOPIC = "hospitalReservation";

    public void sendNotification(NotificationRequest notification) throws InterruptedException {
        log.info("==> sending payload => {} to topic => {}", notification, TOPIC);
        kafkaTemplate.send(TOPIC, "notification", notification);
    }

}
