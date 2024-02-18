package guemri.oc_p11.medhead.hospital.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import guemri.oc_p11.medhead.hospital.controller.HospitalController;
import guemri.oc_p11.medhead.hospital.dao.HospitalAddress;
import guemri.oc_p11.medhead.hospital.dao.HospitalRequest;
import guemri.oc_p11.medhead.hospital.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityResponse;
import guemri.oc_p11.medhead.hospital.service.HospitalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HospitalController.class)
class HospitalControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospitalService hospitalService;

    private final ObjectMapper mapper = new ObjectMapper();

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
            .specialities(Arrays.asList("Anesthesie"))
            .build();

    SpecialityResponse specialityResponse = SpecialityResponse.builder()
            .code("123")
            .specialityName("Anesthesie")
            .specialityGroup("Anesthesie")
            .build();

    HospitalResponse response = HospitalResponse.builder()
            .hospitalRef("456")
            .hospitalName("Leicester General Hospital")
            .hospitalAddress(address)
            .latitude(50.23)
            .longitude(51.45)
            .availableBeds(10)
            .specialities(Arrays.asList(specialityResponse))
            .build();

    @Test
    void test_registerHospital() throws Exception {
        Mockito.when(hospitalService.registerHospital(request)).thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8000/api/v1/hospitals/add")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalRef").exists())
                .andExpect(jsonPath("$.availableBeds").value(10));

    }

    @Test
    void test_getHospitalByName() throws Exception {
        Mockito.when(hospitalService.getHospitalByName("Leicester General Hospital")).thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8000/api/v1/hospitals/name/Leicester General Hospital")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalRef").exists())
                .andExpect(jsonPath("$.hospitalName").value("Leicester General Hospital"));

    }

    @Test
    void test_getHospitalsBySpeciality() throws Exception {
        Mockito.when(hospitalService.getHospitalsBySpeciality("Anesthesie")).thenReturn(Arrays.asList(response));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8000/api/v1/hospitals/speciality/Anesthesie")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void test_getAllHospitals() throws Exception {
        Mockito.when(hospitalService.getAllHospitals()).thenReturn(Arrays.asList(response));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8000/api/v1/hospitals/all")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

}