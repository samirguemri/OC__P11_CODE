package guemri.oc_p11.medhead.hospitalclient.dao;

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
