package guemri.oc_p11.medhead.hospital.dao;

import java.util.List;

public record HospitalRequest(
        String hospitalRef,
        String hospitalName,
        HospitalAddress hospitalAddress,
        int availableBeds,
        List<String> specialities) {
}
