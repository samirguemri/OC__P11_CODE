package guemri.oc_p11.medhead.destination.feignclient.hospital;

import lombok.Builder;

@Builder
public record SpecialityResponse(
        String code,
        String specialityName,
        String specialityGroup) {
}
