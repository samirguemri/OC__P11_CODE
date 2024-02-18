package guemri.oc_p11.medhead.destination.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer hospitalService() {
        return new WireMockServer(8000);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer notificationService() {
        return new WireMockServer(8090);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer googleMapsGeocodingService() {
        return new WireMockServer(wireMockConfig().options().dynamicPort());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer googleMapsRoutingService() {
        return new WireMockServer(wireMockConfig().options().dynamicPort());
    }
}
