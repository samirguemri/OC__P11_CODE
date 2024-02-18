package guemri.oc_p11.medhead.destination.dto;

import guemri.oc_p11.medhead.destination.feignclient.geocodingapi.GeocodingAddressResponse;
import lombok.Builder;

@Builder
public record DestinationRequest(
        String speciality,
        GeocodingAddressResponse.Location location
) {
}
