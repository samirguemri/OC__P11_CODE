package guemri.oc_p11.medhead.hospital.feignclient.geocodingapi;

import guemri.oc_p11.medhead.hospital.configuration.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "google-maps-geocoding",
        url = "https://maps.googleapis.com",
        configuration = ClientConfiguration.class
)
public interface GoogleMapsGeocodingClient {

    @GetMapping(
            value = "/maps/api/geocode/json",
            consumes = "application/json"
    )
    String calculateGeocode(
            @RequestParam(name = "address", required = true) String address,
            @RequestParam(name = "key", required = true) String key
    );
}
