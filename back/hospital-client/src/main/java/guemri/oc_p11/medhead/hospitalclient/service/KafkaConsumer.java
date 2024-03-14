package guemri.oc_p11.medhead.hospitalclient.service;

import guemri.oc_p11.medhead.hospitalclient.entity.Hospital;
import guemri.oc_p11.medhead.hospitalclient.repository.HospitalClientRepository;
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

    @Autowired
    private HospitalClientRepository hospitalClientRepository;

    @KafkaListener(topics = {"hospitalReservation"}, groupId = "medhead-2")
    public void receive(ConsumerRecord<String, guemri.oc_p11.medhead.notification.dto.NotificationRequest> consumerRecord) {
        payload = consumerRecord.toString();
        latch.countDown();
        log.info("==> received notification => {}", consumerRecord);
        NotificationRequest notification = consumerRecord.value();
        log.info("==> {} bed to be reserved for the hospital {}", notification.bedToReserve(), notification.hospitalRef());
        updateHospitalAvailableBeds(notification.hospitalRef(), notification.bedToReserve());
        System.out.println("bed reserved");
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

    private void updateHospitalAvailableBeds(String hospitalRef, int reservation) {
        Hospital hospital = hospitalClientRepository.findById(hospitalRef).get();
        int newAvailableBeds = hospital.getAvailableBeds() - reservation;
        hospital.setAvailableBeds(newAvailableBeds);
        hospitalClientRepository.save(hospital);
    }

}
