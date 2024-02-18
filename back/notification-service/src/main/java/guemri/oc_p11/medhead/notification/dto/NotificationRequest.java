package guemri.oc_p11.medhead.notification.dto;

import lombok.Builder;

@Builder
public record NotificationRequest(
        String hospitalRef,
        String specialityCode,
        Integer bedToReserve
) {
    @Override
    public String toString() {
        return "Notification [ hospitalRef = " + hospitalRef + ", " +
                "specialityCode = " + specialityCode + "," +
                " number of bed = " + bedToReserve + " ]";
    }
}
