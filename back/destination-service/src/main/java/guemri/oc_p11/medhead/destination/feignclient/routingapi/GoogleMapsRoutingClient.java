package guemri.oc_p11.medhead.destination.feignclient.routingapi;

import guemri.oc_p11.medhead.destination.configuration.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "google-maps-routing",
        url = "https://routes.googleapis.com",
        configuration = ClientConfiguration.class
)
public interface GoogleMapsRoutingClient {

    @PostMapping(
            value = "/distanceMatrix/v2:computeRouteMatrix",
            consumes = "application/json"
    )
    String computeRouteMatrix(
            @RequestBody String routeMatrixRequest,
            //@RequestHeader("Content-Type") String contentType,
            @RequestHeader("X-Goog-Api-Key") String apiKey,
            @RequestHeader("X-Goog-FieldMask") String fieldMask
    );
}
