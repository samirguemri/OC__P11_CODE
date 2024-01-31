package guemri.oc_p11.medhead.hospital.feignclient.speciality;

public record SpecialityRequest(
        String code,
        String specialityName,
        String specialityGroup) {
}
