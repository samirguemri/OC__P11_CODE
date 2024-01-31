package guemri.oc_p11.medhead.speciality.dao;

import lombok.Builder;

import java.util.List;

@Builder
public record SpecialityGroupResponse(
        String specialityGroupName,
        List<String> specialities
) {
}
