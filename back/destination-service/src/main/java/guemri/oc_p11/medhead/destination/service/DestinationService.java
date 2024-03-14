package guemri.oc_p11.medhead.destination.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import guemri.oc_p11.medhead.destination.feignclient.hospital.HospitalClient;
import guemri.oc_p11.medhead.destination.feignclient.notification.NotificationClient;
import guemri.oc_p11.medhead.destination.feignclient.notification.NotificationRequest;
import guemri.oc_p11.medhead.destination.feignclient.routingapi.GoogleMapsRoutingClient;
import guemri.oc_p11.medhead.destination.feignclient.routingapi.RouteMatrixResponse;
import guemri.oc_p11.medhead.destination.feignclient.routingapi.RouteMatrixRequest;
import guemri.oc_p11.medhead.destination.feignclient.hospital.HospitalResponse;
import guemri.oc_p11.medhead.destination.dto.DestinationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public record DestinationService(
        HospitalClient hospitalClient,
        GoogleMapsRoutingClient routingClient,
        NotificationClient notificationClient,
        Gson gson
) {
    private static final String googleMapsApiKey = "AIzaSyCDE4qwwbeKMVOsBPtE4lsNjYrRohyIATQ";

    //RouteMatrixRequest routeMatrixRequest
    //RouteMatrixResponse
    public HospitalResponse searchDestination(DestinationRequest destinationRequest) throws InterruptedException {

        // Build the RouteMatrixRequest
        List<RouteMatrixRequest.Origin> origins = List.of(
                RouteMatrixRequest.Origin.builder()
                        .waypoint(new RouteMatrixRequest.Waypoint(new RouteMatrixRequest.Location(
                                new RouteMatrixRequest.LatLng(destinationRequest.location().lat(), destinationRequest.location().lng())
                        )))
                        .routeModifiers(new RouteMatrixRequest.RouteModifiers(true))
                        .build()
        );
        List<HospitalResponse> hospitalResponses =
                hospitalClient.getHospitalsBySpeciality(destinationRequest.speciality()).getBody();
        assert hospitalResponses != null;
        List<RouteMatrixRequest.Destination> destinations =
                hospitalResponses.stream()
                        .map(hospitalResponse -> new RouteMatrixRequest.Destination(
                                new RouteMatrixRequest.Waypoint(new RouteMatrixRequest.Location(
                                        new RouteMatrixRequest.LatLng(hospitalResponse.latitude(), hospitalResponse.longitude())))
                        ))
                        .collect(Collectors.toList());
        RouteMatrixRequest routeMatrixRequest = RouteMatrixRequest.builder()
                .origins(origins)
                .destinations(destinations)
                .travelMode("DRIVE")
                .routingPreference("TRAFFIC_AWARE")
                .build();

        String jsonResponse = routingClient.computeRouteMatrix(
                gson.toJson(routeMatrixRequest),
                googleMapsApiKey,
                "originIndex,destinationIndex,duration,distanceMeters"
        );
        log.info("Request sent to the google-maps-routing service and given back this response {}", jsonResponse);
        Type listType = new TypeToken<List<RouteMatrixResponse.RouteMatrixElement>>() {}.getType();
        List<RouteMatrixResponse.RouteMatrixElement> routeMatrixResponseElements = gson.fromJson(jsonResponse, listType);

        RouteMatrixResponse.RouteMatrixElement nearestHospital =
                routeMatrixResponseElements.stream()
                        .min(Comparator.comparingInt(RouteMatrixResponse.RouteMatrixElement::distanceMeters))
                        .orElseThrow();

        log.info("The nearest hospital is about {} meters", nearestHospital.distanceMeters());

        HospitalResponse selectedHospital = hospitalResponses.get(nearestHospital.destinationIndex());

        // Send notification to the hospital
        NotificationRequest notificationRequest = buildNotificationRequest(selectedHospital.hospitalRef(), destinationRequest.speciality());
        log.info("NoificationRequest to be send to notification-service => {}", notificationRequest);
        ResponseEntity<String> response = notificationClient.sendNotification(notificationRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Notification sent to the selected hospital {}", selectedHospital);
        } else {
            log.info("Issue in sending notification to the selected hospital");
        }
        return selectedHospital;
    }

    private NotificationRequest buildNotificationRequest(String hospitalRef, String speciality) {
        return NotificationRequest.builder()
                .hospitalRef(hospitalRef)
                .speciality(speciality)
                .bedToReserve(1)
                .build();
    }
}
