package guemri.oc_p11.medhead.notification.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import guemri.oc_p11.medhead.notification.service.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@Testcontainers
class NotificationApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private KafkaConsumer consumer;

    private final ObjectMapper mapper = new ObjectMapper();

    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
                    .withKraft()
                    .withExposedPorts(9092,9093);

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeEach
    public void setup() {
        consumer.resetLatch();
    }

    @Test
    void test_sendNotification() throws Exception {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .hospitalRef("123")
                .specialityCode("456")
                .bedToReserve(1)
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8090/api/v1/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notificationRequest))
                .accept(MediaType.TEXT_PLAIN);

        this.mockMvc.perform(requestBuilder);

    }
}
