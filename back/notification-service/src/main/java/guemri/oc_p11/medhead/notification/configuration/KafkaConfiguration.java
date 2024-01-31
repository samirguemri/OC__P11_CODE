package guemri.oc_p11.medhead.notification.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    public static final String TOPIC = "hospitalReservation";

    @Bean
    public NewTopic topicBuilder() {
        return TopicBuilder.name(TOPIC)
                .build();
    }
}
