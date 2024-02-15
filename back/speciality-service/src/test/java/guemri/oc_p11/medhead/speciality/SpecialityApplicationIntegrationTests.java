package guemri.oc_p11.medhead.speciality;

import com.fasterxml.jackson.databind.ObjectMapper;
import guemri.oc_p11.medhead.speciality.controller.SpecialityController;
import guemri.oc_p11.medhead.speciality.dao.SpecialityRequest;
import guemri.oc_p11.medhead.speciality.service.SpecialityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SpecialityApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @Test
    public void test_insertSpeciality() throws Exception {

        SpecialityRequest request = SpecialityRequest.builder()
                .specialityName("Anesthesie")
                .specialityGroup("Anesthesie")
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("https://localhost:9443/api/v1/specialities/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.specialityName").value("Anesthesie"));
    }

    @Test
    void test_getSpecialityByName() throws Exception {

        SpecialityRequest request = SpecialityRequest.builder()
                .specialityName("Anesthesie")
                .specialityGroup("Anesthesie")
                .build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("https://localhost:9443/api/v1/specialities/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder);

        requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/speciality/name/Anesthesie")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialityName").value("Anesthesie"));
    }
}
