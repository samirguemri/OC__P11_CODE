package guemri.oc_p11.medhead.hospitalclient.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import guemri.oc_p11.medhead.hospitalclient.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospitalclient.entity.Hospital;
import guemri.oc_p11.medhead.hospitalclient.repository.HospitalClientRepository;

@Service
public record hospitalClientService(HospitalClientRepository hospitalClientRepository) {

    public List<HospitalResponse> getAllHospitals() {
        List<Hospital> hospitals = hospitalClientRepository.findAll();
        return hospitals.stream()
                .map(hospital -> hospitalResponseBuilder(hospital))
                .collect(Collectors.toList());
    }

    private HospitalResponse hospitalResponseBuilder(Hospital hospital) {
                return HospitalResponse.builder()
                    .hospitalRef(hospital.getHospitalRef())
                    .hospitalName(hospital.getHospitalName())
                    .hospitalAddress(hospital.getHospitalAddress())
                    .latitude(hospital.getLatitude())
                    .longitude(hospital.getLongitude())
                    .availableBeds(hospital.getAvailableBeds())
                    .specialities(hospital.getSpecialities())
                    .build();
    }

}
