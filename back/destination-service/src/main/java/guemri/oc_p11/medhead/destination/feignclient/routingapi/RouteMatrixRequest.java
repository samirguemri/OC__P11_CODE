package guemri.oc_p11.medhead.destination.feignclient.routingapi;

import lombok.Builder;

import java.util.List;

@Builder
public record RouteMatrixRequest(

        List<Origin> origins,
        List<Destination> destinations,
        String travelMode,
        String routingPreference
) {

    @Builder
    public record Origin(
        Waypoint waypoint,
        RouteModifiers routeModifiers
    ) {}

    @Builder
    public record Destination(
            Waypoint waypoint
    ) {}

    @Builder
    public record Waypoint(
            Location location
    ) {}

    @Builder
    public record Location(
            LatLng latLng
    ) {}

    @Builder
    public record LatLng(
            double latitude,
            double longitude
    ) {}

    @Builder
    public record RouteModifiers(
            boolean avoidFerries
    ) {}

}
