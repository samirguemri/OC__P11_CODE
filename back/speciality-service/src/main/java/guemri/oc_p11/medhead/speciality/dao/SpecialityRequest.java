package guemri.oc_p11.medhead.speciality.dao;

import lombok.Builder;

@Builder
public record SpecialityRequest(
        String specialityName,
        String specialityGroup) {
}
