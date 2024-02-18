package guemri.oc_p11.medhead.hospital.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import guemri.oc_p11.medhead.hospital.dao.HospitalAddress;
import guemri.oc_p11.medhead.hospital.dao.HospitalRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.IOException;
import java.util.Arrays;

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
class HospitalApplicationIntegrationTests {

    private static final String googleMapsApiKey = "AIzaSyCDE4qwwbeKMVOsBPtE4lsNjYrRohyIATQ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("specialityService")
    private WireMockServer specialityService;

    @Autowired
    @Qualifier("googleMapsGeocodingService")
    private WireMockServer googleMapsGeocodingService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @BeforeEach
    void setupStub() throws IOException {

        this.specialityService.stubFor(get(urlEqualTo("/api/v1/specialities/get/list?speciality=Anesthesie&speciality=Cardiologie"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(copyToString(
                                new ClassPathResource("payload/get-specialityService-response.json").getInputStream(),
                                defaultCharset())
                        )));

        this.googleMapsGeocodingService.stubFor(get(urlEqualTo("/maps/api/geocode/json?address=Leicester General Hospital, Gwendolen Rd, Leicester, LE5 4PW, United Kingdom&key="+this.googleMapsApiKey))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(copyToString(
                                new ClassPathResource("payload/get-specialityService-response.json").getInputStream(),
                                defaultCharset())
                        )));
    }

    @Test
    void test_registerHospital() throws Exception {

        HospitalAddress address = HospitalAddress.builder()
                .numberAndStreet("Gwendolen Rd")
                .locality("Leicester")
                .postCode("LE5 4PW")
                .country("United Kingdom")
                .build();

        HospitalRequest request = HospitalRequest.builder()
                .hospitalName("Leicester General Hospital")
                .hospitalAddress(address)
                .availableBeds(10)
                .specialities(Arrays.asList("Anesthesie", "Cardiologie"))
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8000/api/v1/hospitals/add")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalRef").exists())
                .andExpect(jsonPath("$.hospitalName").value("Leicester General Hospital"));
    }

    @Test
    void test_getHospitalByName() throws Exception {

        HospitalAddress address = HospitalAddress.builder()
                .numberAndStreet("Gwendolen Rd")
                .locality("Leicester")
                .postCode("LE5 4PW")
                .country("United Kingdom")
                .build();

        HospitalRequest request = HospitalRequest.builder()
                .hospitalName("Leicester General Hospital")
                .hospitalAddress(address)
                .availableBeds(10)
                .specialities(Arrays.asList("Anesthesie", "Cardiologie"))
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8000/api/v1/hospitals/add")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .get("http://localhost:8000/api/v1/hospitals/name/Leicester General Hospital")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder2)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalRef").exists())
                .andExpect(jsonPath("$.hospitalName").value("Leicester General Hospital"));

    }
}