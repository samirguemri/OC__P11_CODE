package guemri.oc_p11.medhead.destination.feignclient.hospital;

import java.util.List;

public record HospitalRequest(
        String hospitalRef,
        String hospitalName,
        HospitalAddress hospitalAddress,
        int availableBeds,
        List<String> specialities) {
}
