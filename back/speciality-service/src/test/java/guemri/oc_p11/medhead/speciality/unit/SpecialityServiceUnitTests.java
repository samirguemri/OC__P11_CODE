package guemri.oc_p11.medhead.speciality.unit;

import guemri.oc_p11.medhead.speciality.dao.SpecialityGroupResponse;
import guemri.oc_p11.medhead.speciality.dao.SpecialityRequest;
import guemri.oc_p11.medhead.speciality.dao.SpecialityResponse;
import guemri.oc_p11.medhead.speciality.entity.Speciality;
import guemri.oc_p11.medhead.speciality.repository.SpecialityRepository;
import guemri.oc_p11.medhead.speciality.service.SpecialityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SpecialityServiceUnitTests {

    @Mock
    private SpecialityRepository specialityRepository;
    @InjectMocks
    private SpecialityService specialityService;

    @Test
    void test_insertSpeciality() {
        // Mocking the repository Layer
        Speciality specialityToSave = Speciality.builder()
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        Speciality savedSpeciality = Speciality.builder()
                .code("123")
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        Mockito.when(specialityRepository.insert(specialityToSave)).thenReturn(savedSpeciality);

        // Executing the service method
        SpecialityRequest request = SpecialityRequest.builder()
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        SpecialityResponse response = specialityService.insertSpeciality(request);

        // Asserting the results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.code()).isEqualTo("123");
        Assertions.assertThat(response.specialityName()).isEqualTo("Parodontie");
    }

    @Test
    void test_insertBulkSpeciality() {
        // Mock repository Layer
        Speciality specialityToSave1 = Speciality.builder()
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Speciality specialityToSave2 = Speciality.builder()
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        List<Speciality> specialitiesToSave = Arrays.asList(specialityToSave1, specialityToSave2);

        Speciality savedSpeciality1 = Speciality.builder()
                .code("123")
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Speciality savedSpeciality2 = Speciality.builder()
                .code("456")
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        List<Speciality> savedSpecialities = Arrays.asList(savedSpeciality1, savedSpeciality2);
        Mockito.when(specialityRepository.insert(specialitiesToSave)).thenReturn(savedSpecialities);

        // Execute tests
        List<SpecialityRequest> requests = new ArrayList<>();
        requests.add(SpecialityRequest.builder()
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build());
        requests.add(SpecialityRequest.builder()
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build());
        List<SpecialityResponse> responses = specialityService.insertBulkSpeciality(requests);

        // Assert results
        Assertions.assertThat(responses).isNotNull();
        Assertions.assertThat(responses).hasSize(requests.size());
        Assertions.assertThat(responses.get(0).code()).isIn("123","456");
        Assertions.assertThat(responses.get(1).code()).isIn("123","456");
    }

    @Test
    void test_getSpecialityByCode() {
        // Mock repository Layer
        String code = "123";
        Speciality foundSpeciality = Speciality.builder()
                .code(code)
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Mockito.when(specialityRepository.findById(code)).thenReturn(Optional.of(foundSpeciality));

        // Execute tests
        SpecialityResponse response = specialityService.getSpecialityByCode(code);

        // Assert results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.code()).isEqualTo(code);

        assertThrows(NoSuchElementException.class, () -> {
            specialityService.getSpecialityByCode("456");
        });
    }

    @Test
    void test_getSpecialityByName() {
        // Mock repository Layer
        String name = "Anesthésie";
        Speciality foundSpeciality = Speciality.builder()
                .code("123")
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Mockito.when(specialityRepository.findSpecialityBySpecialityName(name)).thenReturn(Optional.of(foundSpeciality));

        // Execute tests
        SpecialityResponse response = specialityService.getSpecialityByName(name);

        // Assert results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.specialityName()).isEqualTo(name);

        assertThrows(NoSuchElementException.class, () -> {
            specialityService.getSpecialityByName("Parodontie");
        });
    }

    @Test
    void test_getSpecialitiesFromList() {
        // Mock repository Layer
        Speciality speciality1 = Speciality.builder()
                .code("123")
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Mockito.when(specialityRepository.findSpecialityBySpecialityName("Anesthésie")).thenReturn(Optional.of(speciality1));
        Speciality speciality2 = Speciality.builder()
                .code("456")
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        Mockito.when(specialityRepository.findSpecialityBySpecialityName("Parodontie")).thenReturn(Optional.of(speciality2));

        // Execute tests
        List<String> names = Arrays.asList("Anesthésie", "Parodontie");
        List<SpecialityResponse> responses = specialityService.getSpecialitiesFromList(names);

        // Assert results
        Assertions.assertThat(responses).isNotNull();
        Assertions.assertThat(responses).hasSize(names.size());
    }

    @Test
    void test_getAllSpecialities() {
        // Mock repository Layer
        Speciality speciality1 = Speciality.builder()
                .code("123")
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Speciality speciality2 = Speciality.builder()
                .code("456")
                .specialityName("Parodontie")
                .specialityGroup("Groupe dentaire")
                .build();
        List<Speciality> allSpecialities = Arrays.asList(speciality1, speciality2);
        Mockito.when(specialityRepository.findAll()).thenReturn(allSpecialities);

        // Execute tests
        List<SpecialityResponse> responses = specialityService.getAllSpecialities();

        // Assert results
        Assertions.assertThat(responses).isNotNull();
        Assertions.assertThat(responses).hasSize(allSpecialities.size());
    }

    @Test
    void test_getSpecialitiesByGroupName() {
        // Mock repository Layer
        Speciality speciality1 = Speciality.builder()
                .code("123")
                .specialityName("Anesthésie")
                .specialityGroup("Anesthésie")
                .build();
        Speciality speciality2 = Speciality.builder()
                .code("456")
                .specialityName("Soins intensifs")
                .specialityGroup("Anesthésie")
                .build();
        List<Speciality> specialitiesInGroup = Arrays.asList(speciality1, speciality2);
        Mockito.when(specialityRepository.findSpecialitiesBySpecialityGroup("Anesthésie")).thenReturn(Optional.of(specialitiesInGroup));

        // Execute tests
        SpecialityGroupResponse response = specialityService.getSpecialitiesByGroupName("Anesthésie");

        // Assert results
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.specialityGroupName()).isEqualTo("Anesthésie");
        Assertions.assertThat(response.specialities()).hasSize(specialitiesInGroup.size());
    }
}