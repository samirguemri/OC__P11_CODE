package guemri.oc_p11.medhead.speciality;

import guemri.oc_p11.medhead.speciality.controller.SpecialityController;
import guemri.oc_p11.medhead.speciality.service.SpecialityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(SpecialityController.class)
public class SpecialityApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialityService specialityService;

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

    String jsonRequest = "{\"specialityName\":\"Anesthesie\",\"specialityGroup\":\"Anesthesie\"}";
    String jsonResponse = "{\"code\":\"123\",\"specialityName\":\"Anesthesie\",\"specialityGroup\":\"Anesthesie\"}";

    @Test
    public void test_insertSpeciality() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("https://localhost:9443/api/v1/specialities/post")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                /*.andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.specialityName").value("Anesthesie"))*/;

    }
}
