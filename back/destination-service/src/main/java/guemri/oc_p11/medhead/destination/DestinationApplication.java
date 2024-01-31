package guemri.oc_p11.medhead.destination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DestinationApplication {
    public static void main(String[] args) {
        SpringApplication.run(DestinationApplication.class, args);
    }
}
