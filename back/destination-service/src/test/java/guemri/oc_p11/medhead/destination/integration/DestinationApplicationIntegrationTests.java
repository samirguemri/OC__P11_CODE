package guemri.oc_p11.medhead.destination.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import guemri.oc_p11.medhead.destination.dto.DestinationRequest;
import guemri.oc_p11.medhead.destination.feignclient.geocodingapi.GeocodingAddressResponse;
import guemri.oc_p11.medhead.destination.feignclient.notification.NotificationRequest;
import guemri.oc_p11.medhead.destination.feignclient.routingapi.RouteMatrixRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@EnableFeignClients
@ContextConfiguration(classes = { WireMockConfig.class })
class DestinationApplicationIntegrationTests {

    private static final String googleMapsApiKey = "AIzaSyCDE4qwwbeKMVOsBPtE4lsNjYrRohyIATQ";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    Gson gson;

    private final ObjectMapper mapper = new ObjectMapper();


    @Autowired
    @Qualifier("hospitalService")
    private WireMockServer hospitalService;

    @Autowired
    @Qualifier("notificationService")
    private WireMockServer notificationService;

    @Autowired
    @Qualifier("googleMapsRoutingService")
    private WireMockServer googleMapsRoutingService;

    @BeforeEach
    void setupStub() throws IOException {

        this.hospitalService.stubFor(get(urlEqualTo("/api/v1/hospitals/speciality/Anesthesie"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(copyToString(
                                new ClassPathResource("payload/get-hospitalService-response.json").getInputStream(),
                                defaultCharset())
                        )));

        this.googleMapsRoutingService.stubFor(post(urlEqualTo("/distanceMatrix/v2:computeRouteMatrix"))
                .withHeader("X-Goog-Api-Key", equalTo(googleMapsApiKey))
                .withHeader("X-Goog-FieldMask", equalTo("originIndex,destinationIndex,duration,distanceMeters"))
                .withRequestBody(equalTo(routeMatrixRequestJson()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(copyToString(
                                new ClassPathResource("payload/get-hospitalService-response.json").getInputStream(),
                                defaultCharset())
                        )));

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .hospitalRef("123")
                .speciality("Anesthesie")
                .bedToReserve(1)
                .build();

        this.notificationService.stubFor(post(urlEqualTo("/api/v1/notification/send"))
                .withRequestBody(equalTo(mapper.writeValueAsString(notificationRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody("Notification sent successfully")));
    }

    @Test
    void test_searchDestination() throws Exception {

        DestinationRequest destinationRequest = DestinationRequest.builder()
                .speciality("Anesthesie")
                .location(GeocodingAddressResponse.Location.builder()
                        .lat(52.5171114)
                        .lng(-0.9834831)
                        .build())
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/api/v1/destination/search")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(destinationRequest))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalName").value("Leicester General Hospital"));

    }

    private String routeMatrixRequestJson() throws IOException {
        return copyToString(
                new ClassPathResource("payload/get-googleMapsRoutingService-request.json").getInputStream(),
                defaultCharset());
    }

    private String routeMatrixResponseJson() throws IOException {
        return copyToString(
                new ClassPathResource("payload/get-googleMapsRoutingService-response.json").getInputStream(),
                defaultCharset());
    }

}