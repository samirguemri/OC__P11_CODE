package guemri.oc_p11.medhead.hospital.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import guemri.oc_p11.medhead.hospital.dao.HospitalAddress;
import guemri.oc_p11.medhead.hospital.dao.HospitalRequest;
import guemri.oc_p11.medhead.hospital.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospital.entity.Hospital;
import guemri.oc_p11.medhead.hospital.feignclient.geocodingapi.GeocodingAddressResponse;
import guemri.oc_p11.medhead.hospital.feignclient.geocodingapi.GoogleMapsGeocodingClient;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityClient;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityResponse;
import guemri.oc_p11.medhead.hospital.repository.HospitalRepository;
import guemri.oc_p11.medhead.hospital.service.HospitalService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HospitalServiceUnitTests {

    @Mock
    private HospitalRepository hospitalRepository;
    @Mock
    SpecialityClient specialityClient;
    @Mock
    GoogleMapsGeocodingClient geocodingClient;
    @Mock
    Gson gson;

    @InjectMocks
    private HospitalService hospitalService;

    private ObjectMapper mapper = new ObjectMapper();

    private static Hospital savedHospital;
    private static Hospital hospitalToSave;
    private static HospitalRequest hospitalRequest;
    private static SpecialityResponse specialityResponse;
    private static HospitalAddress address;

    @BeforeAll
    public static void setUp() {
        address = HospitalAddress.builder()
                .numberAndStreet("Gwendolen Rd")
                .locality("Leicester")
                .postCode("LE5 4PW")
                .country("United Kingdom")
                .build();

        hospitalRequest = HospitalRequest.builder()
                .hospitalName("Leicester General Hospital")
                .hospitalAddress(address)
                .availableBeds(10)
                .specialities(Arrays.asList("Anesthesie"))
                .build();

        specialityResponse = SpecialityResponse.builder()
                .code("123")
                .specialityName("Anesthesie")
                .specialityGroup("Anesthesie")
                .build();

        hospitalToSave = Hospital.builder()
                .hospitalName("Leicester General Hospital")
                .hospitalAddress(address)
                .latitude(50.5)
                .longitude(52.6)
                .availableBeds(10)
                .specialities(Arrays.asList(specialityResponse))
                .build();
        savedHospital = Hospital.builder()
                .hospitalRef("456")
                .hospitalName("Leicester General Hospital")
                .hospitalAddress(address)
                .latitude(50.5)
                .longitude(52.6)
                .availableBeds(10)
                .specialities(Arrays.asList(specialityResponse))
                .build();
    }

    @Test
    void test_registerHospital() throws Exception {
        // Mocking the speciality-service
        Mockito.when(specialityClient.getSpecialitiesFromList(Arrays.asList("Anesthesie")))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(Arrays.asList(specialityResponse)));

        // Mocking the googleapi-service
        Mockito.when(geocodingClient.calculateGeocode(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("jsonResponse");
        Mockito.when(gson.fromJson("jsonResponse", GeocodingAddressResponse.class))
                .thenReturn(mockGeocodingAddressResponse());

        // Mocking the repository Layer
        Mockito.when(hospitalRepository.save(hospitalToSave)).thenReturn(savedHospital);

        // Execute tests
        HospitalResponse response = hospitalService.registerHospital(hospitalRequest);

        // Assert results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.hospitalRef()).isNotEmpty();
        Assertions.assertThat(response.hospitalName()).isEqualTo("Leicester General Hospital");

    }

    @Test
    void test_getHospitalByName() throws Exception {
        // Mocking the repository Layer
        Mockito.when(hospitalRepository.findHospitalByHospitalName("Leicester General Hospital")).thenReturn(Optional.of(savedHospital));

        // Execute tests
        HospitalResponse response = hospitalService.getHospitalByName("Leicester General Hospital");

        // Assert results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.hospitalRef()).isNotEmpty();
        Assertions.assertThat(response.hospitalName()).isEqualTo("Leicester General Hospital");
    }

    @Test
    void test_getHospitalsBySpeciality() throws Exception{
        // Mocking the repository Layer
        Mockito.when(hospitalRepository.findAll()).thenReturn(List.of(savedHospital));

        // Execute tests
        List<HospitalResponse> responses = hospitalService.getHospitalsBySpeciality("Anesthesie");

        // Assert results
        Assertions.assertThat(responses).isNotEmpty();
        Assertions.assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void test_getAllHospitals() throws Exception {
        // Mocking the repository Layer
        Mockito.when(hospitalRepository.findAll()).thenReturn(List.of(savedHospital));

        // Execute tests
        List<HospitalResponse> responses = hospitalService.getAllHospitals();

        // Assert results
        Assertions.assertThat(responses).isNotEmpty();
        Assertions.assertThat(responses.get(0).hospitalName()).isEqualTo("Leicester General Hospital");
    }

    private GeocodingAddressResponse mockGeocodingAddressResponse() throws JsonProcessingException {
        return GeocodingAddressResponse.builder()
                .status(String.valueOf(HttpStatus.OK))
                .results(List.of(
                        new GeocodingAddressResponse.Result(
                                null,
                                "Formatted Address",
                                new GeocodingAddressResponse.Geometry(
                                        null,
                                        new GeocodingAddressResponse.Location(50.5, 52.6),
                                        "Location Type",
                                        null
                                ),
                                "Place ID",
                                null,
                                null
                        )
                ))
                .build();
    }

}