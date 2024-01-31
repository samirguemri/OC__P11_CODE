package guemri.oc_p11.medhead.hospital.dao;

import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record HospitalResponse(
        String hospitalRef,
        String hospitalName,
        HospitalAddress hospitalAddress,
        double latitude,
        double longitude,
        int availableBeds,
        List<SpecialityResponse> specialities) {
}
