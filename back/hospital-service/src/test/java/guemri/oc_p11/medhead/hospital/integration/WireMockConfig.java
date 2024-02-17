package guemri.oc_p11.medhead.hospital.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer specialityService() {
        return new WireMockServer(9000);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer googleMapsGeocodingService() {
        return new WireMockServer(wireMockConfig().options().dynamicPort());
    }
}
