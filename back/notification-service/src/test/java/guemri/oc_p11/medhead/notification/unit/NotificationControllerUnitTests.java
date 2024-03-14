package guemri.oc_p11.medhead.notification.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import guemri.oc_p11.medhead.notification.controller.NotificationController;
import guemri.oc_p11.medhead.notification.dto.NotificationRequest;
import guemri.oc_p11.medhead.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private final ObjectMapper mapper = new ObjectMapper();

    private NotificationRequest notificationRequest;

    @BeforeEach
    void setUp() {
        notificationRequest = NotificationRequest.builder()
                .hospitalRef("123")
                .speciality("456")
                .bedToReserve(1)
                .build();
    }

    @Test
    void test_sendNotification() throws Exception {
        //Mockito.when(notificationService.sendNotification(notificationRequest)).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8090/api/v1/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notificationRequest))
                .accept(MediaType.TEXT_PLAIN);

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Notification sent"));
    }
}