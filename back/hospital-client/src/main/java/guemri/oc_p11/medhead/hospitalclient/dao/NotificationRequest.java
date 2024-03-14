package guemri.oc_p11.medhead.hospitalclient.dao;

import lombok.Builder;

@Builder
public record NotificationRequest(
        String hospitalRef,
        String speciality,
        Integer bedToReserve
) {}
