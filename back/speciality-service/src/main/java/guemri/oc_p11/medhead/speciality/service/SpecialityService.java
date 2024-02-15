package guemri.oc_p11.medhead.speciality.service;

import guemri.oc_p11.medhead.speciality.dao.SpecialityRequest;
import guemri.oc_p11.medhead.speciality.dao.SpecialityResponse;
import guemri.oc_p11.medhead.speciality.dao.SpecialityGroupResponse;
import guemri.oc_p11.medhead.speciality.entity.Speciality;
import guemri.oc_p11.medhead.speciality.repository.SpecialityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public record SpecialityService(SpecialityRepository specialityRepository) {
    public SpecialityResponse insertSpeciality(SpecialityRequest request) {

        Speciality newSpeciality = Speciality.builder()
                .specialityName(request.specialityName())
                .specialityGroup(request.specialityGroup())
                .build();

        Speciality speciality = specialityRepository.insert(newSpeciality);
        log.info("SpecialityService::insertSpeciality method executed");

        return SpecialityResponse.builder()
                .code(speciality.getCode())
                .specialityName(speciality.getSpecialityName())
                .specialityGroup(speciality.getSpecialityGroup())
                .build();
    }

    public List<SpecialityResponse> insertBulkSpeciality(List<SpecialityRequest> requests) {
        List<Speciality> specialities = requests.stream()
                        .map(specialityRequest ->
                                Speciality.builder()
                                        .specialityName(specialityRequest.specialityName())
                                        .specialityGroup(specialityRequest.specialityGroup())
                                        .build()
                        ).collect(Collectors.toList());
        return specialityRepository.insert(specialities).stream()
                .map(speciality ->
                        SpecialityResponse.builder()
                                .code(speciality.getCode())
                                .specialityName(speciality.getSpecialityName())
                                .specialityGroup(speciality.getSpecialityGroup())
                                .build()
                ).collect(Collectors.toList());
    }

    public SpecialityResponse getSpecialityByCode(String code) {
        Speciality speciality = specialityRepository.findById(code).orElseThrow();//findSpecialityByCode
        return SpecialityResponse.builder()
                .code(speciality.getCode())
                .specialityName(speciality.getSpecialityName())
                .specialityGroup(speciality.getSpecialityGroup())
                .build();
    }

    public SpecialityResponse getSpecialityByName(String name) {
        Speciality speciality = specialityRepository.findSpecialityBySpecialityName(name).orElseThrow();
        return SpecialityResponse.builder()
                .code(speciality.getCode())
                .specialityName(speciality.getSpecialityName())
                .specialityGroup(speciality.getSpecialityGroup())
                .build();
    }

    public List<SpecialityResponse> getSpecialitiesFromList(List<String> specialities) {
        List<SpecialityResponse> specialityResponseList = new ArrayList<>();
        specialities.forEach(specialityName -> {
            SpecialityResponse response = getSpecialityByName(specialityName);
            specialityResponseList.add(response);
        });
        return specialityResponseList;
    }

    public List<SpecialityResponse> getAllSpecialities() {
        return specialityRepository.findAll().stream()
                .map(speciality ->
                        SpecialityResponse.builder()
                                .code(speciality.getCode())
                                .specialityName(speciality.getSpecialityName())
                                .specialityGroup(speciality.getSpecialityGroup())
                                .build()
                ).collect(Collectors.toList());
    }

    public SpecialityGroupResponse getSpecialitiesByGroupName(String groupName) {
        List<Speciality> specialities = specialityRepository.findSpecialitiesBySpecialityGroup(groupName).orElseThrow();
        SpecialityGroupResponse specialityGroupResponse = new SpecialityGroupResponse(
                specialities.get(0).getSpecialityGroup(),
                new ArrayList<>()
        );
        specialities.forEach(speciality ->
                specialityGroupResponse.specialities().add(speciality.getSpecialityName()));
        return specialityGroupResponse;
    }
}
