package guemri.oc_p11.medhead.hospital.feignclient.speciality;

import lombok.Builder;

@Builder
public record SpecialityResponse(
        String code,
        String specialityName,
        String specialityGroup) {
}
