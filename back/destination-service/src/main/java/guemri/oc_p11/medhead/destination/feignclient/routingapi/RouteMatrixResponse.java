package guemri.oc_p11.medhead.destination.feignclient.routingapi;

import lombok.Builder;

import java.util.List;
@Builder
public record RouteMatrixResponse(
        List<RouteMatrixElement> routeMatrixElements
        ) {
    public record RouteMatrixElement(
            int originIndex,
            int destinationIndex,
            int distanceMeters,
            String duration
    ) {}
}
