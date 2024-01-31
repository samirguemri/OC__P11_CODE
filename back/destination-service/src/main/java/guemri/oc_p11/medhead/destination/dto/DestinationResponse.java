package guemri.oc_p11.medhead.destination.dto;


import guemri.oc_p11.medhead.destination.feignclient.hospital.HospitalAddress;
import lombok.Builder;

@Builder
public record DestinationResponse(
        String hospitalRef,
        String hospitalName,
        HospitalAddress hospitalAddress
) {
}
