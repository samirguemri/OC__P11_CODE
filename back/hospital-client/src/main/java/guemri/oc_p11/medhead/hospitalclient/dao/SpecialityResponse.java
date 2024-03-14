package guemri.oc_p11.medhead.hospitalclient.dao;

import lombok.Builder;

@Builder
public record SpecialityResponse(
        String code,
        String specialityName,
        String specialityGroup) {
}
