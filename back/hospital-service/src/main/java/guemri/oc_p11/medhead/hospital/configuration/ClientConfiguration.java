package guemri.oc_p11.medhead.hospital.configuration;

import feign.okhttp.OkHttpClient;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
