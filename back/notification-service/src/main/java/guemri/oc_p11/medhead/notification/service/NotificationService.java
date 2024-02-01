package guemri.oc_p11.medhead.notification.service;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public record NotificationService(
        KafkaTemplate<String, Object> kafkaTemplate
) {

    public static final String TOPIC = "hospitalReservation";

    public boolean sendNotification(NotificationRequest notification) throws InterruptedException {

        log.info(String.format("Notification sent -> %s", notification));
        CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(TOPIC, notification);

        // Asynchronously handle the result
        completableFuture.thenAccept(sendResult -> {
            // Success callback
            System.out.println("Message sent successfully with offset: " + sendResult.getRecordMetadata().offset());
        }).exceptionally(exception -> {
            // Exception callback
            System.out.println("Failed to send message with exception: " + exception.getMessage());
            return null;
        });
        return true;
    }

}
