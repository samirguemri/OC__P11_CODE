package guemri.oc_p11.medhead.notification.service;

import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class KafkaConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = {"hospitalReservation"}, groupId = "medhead")
    public void receive(ConsumerRecord<String, NotificationRequest> consumerRecord) {
        payload = consumerRecord.toString();
        latch.countDown();
        log.info("==> received payload => {}", consumerRecord);
        NotificationRequest notification = consumerRecord.value();
        System.out.println("==> received notification => " + notification.hospitalRef() + " " + notification.bedToReserve());
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public String getPayload() {
        return payload;
    }

    public CountDownLatch getLatch() {
        return latch;
    }


}
