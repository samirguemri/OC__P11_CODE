package guemri.oc_p11.medhead.destination.feignclient.notification;

import lombok.Builder;

@Builder
public record NotificationRequest(
        String hospitalRef,
        String specialityCode,
        Integer number
) {
}
