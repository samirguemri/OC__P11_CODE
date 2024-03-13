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

        /*log.info(String.format("Notification sent -> %s", notification));
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

        try {
            kafkaTemplate.send(TOPIC, notification).get(); // Waits for the send operation to complete
            return true; // Return true if send succeeds
        } catch (Exception e) {
            log.error("Failed to send message with exception: " + e.getMessage());
            return false; // Return false if send fails
        }

        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, notification);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent notification=[" + notification +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send notification=[" +
                            notification + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : "+ ex.getMessage());
        }*/
        log.info("sending payload => {} to topic => {}", notification, TOPIC);
        kafkaTemplate.send(TOPIC, notification);
    }

}
