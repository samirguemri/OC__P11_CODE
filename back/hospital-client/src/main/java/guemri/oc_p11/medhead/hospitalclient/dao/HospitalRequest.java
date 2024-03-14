package guemri.oc_p11.medhead.hospitalclient.dao;

import lombok.Builder;

import java.util.List;

@Builder
public record HospitalRequest(
        String hospitalRef,
        String hospitalName,
        HospitalAddress hospitalAddress,
        int availableBeds,
        List<String> specialities) {
}
