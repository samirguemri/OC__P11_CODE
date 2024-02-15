package guemri.oc_p11.medhead.speciality.controller;

import guemri.oc_p11.medhead.speciality.dao.SpecialityGroupResponse;
import guemri.oc_p11.medhead.speciality.dao.SpecialityRequest;
import guemri.oc_p11.medhead.speciality.dao.SpecialityResponse;
import guemri.oc_p11.medhead.speciality.service.SpecialityService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@WebMvcTest(SpecialityController.class)
public class SpecialityControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialityService specialityService;

    SpecialityRequest request = SpecialityRequest.builder()
            .specialityName("Anesthesie")
            .specialityGroup("Anesthesie")
            .build();
    SpecialityResponse response = SpecialityResponse.builder()
            .code("123")
            .specialityName("Anesthesie")
            .specialityGroup("Anesthesie")
            .build();
    String jsonRequest = "{\"specialityName\":\"Anesthesie\",\"specialityGroup\":\"Anesthesie\"}";
    String jsonResponse = "{\"code\":\"123\",\"specialityName\":\"Anesthesie\",\"specialityGroup\":\"Anesthesie\"}";

    @Test
    public void test_insertSpeciality() throws Exception {
        Mockito.when(specialityService.insertSpeciality(request)).thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("https://localhost:9443/api/v1/specialities/post")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(jsonResponse, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void test_insertBulkSpeciality() throws Exception {
        Mockito.when(specialityService.insertBulkSpeciality(Arrays.asList(request))).thenReturn(Arrays.asList(response));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("https://localhost:9443/api/v1/specialities/postAll")
                .accept(MediaType.APPLICATION_JSON)
                .content("["+jsonRequest+"]")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String listResponse = "["+jsonResponse+"]";
        JSONAssert.assertEquals(listResponse, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void test_getSpecialityById() throws Exception {

        Mockito.when(specialityService.getSpecialityByCode(Mockito.anyString())).thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/speciality/code/123")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(jsonResponse, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void test_getSpecialityByName() throws Exception {
        Mockito.when(specialityService.getSpecialityByName(Mockito.anyString())).thenReturn(response);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/speciality/name/Anesthesie")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(jsonResponse, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void test_getSpecialitiesFromList()  throws Exception {
        Mockito.when(specialityService.getSpecialitiesFromList(Arrays.asList("Anesthesie"))).thenReturn(Arrays.asList(response));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/list?speciality=Anesthesie")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String listResponse = "["+jsonResponse+"]";
        JSONAssert.assertEquals(listResponse, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void test_getAllSpecialities() throws Exception {
        Mockito.when(specialityService.getAllSpecialities()).thenReturn(Arrays.asList(response));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/all")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String listResponse = "["+jsonResponse+"]";
        JSONAssert.assertEquals(listResponse, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void test_getSpecialitiesByGroupName() throws Exception {
        SpecialityGroupResponse groupResponse = SpecialityGroupResponse.builder()
                .specialityGroupName("Anesthesie")
                .specialities(Arrays.asList("Anesthesie"))
                .build();
        Mockito.when(specialityService.getSpecialitiesByGroupName("Anesthesie")).thenReturn(groupResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("https://localhost:9443/api/v1/specialities/get/group/Anesthesie")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String groupResponseJson = "{\"specialityGroupName\":\"Anesthesie\",\"specialities\":[\"Anesthesie\"]}";
        JSONAssert.assertEquals(groupResponseJson, result.getResponse().getContentAsString(), false);
    }

}
