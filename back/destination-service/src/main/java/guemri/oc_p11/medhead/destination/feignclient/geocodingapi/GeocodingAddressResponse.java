package guemri.oc_p11.medhead.destination.feignclient.geocodingapi;

import lombok.Builder;

import java.util.List;

@Builder
public record GeocodingAddressResponse(
        List<Result> results,
        String status
) {
    public record Result(
            List<AddressComponent> address_components,
            String formatted_address,
            Geometry geometry,
            String place_id,
            PlusCode plus_code,
            List<String> types
    ) {}
    public record AddressComponent(
            String long_name,
            String short_name,
            List<String> types
    ) {}
    public record Geometry(
            Bounds bounds,
            Location location,
            String location_type,
            Viewport viewport
    ) {}
    public record Bounds(
            Location northeast,
            Location southwest
    ) {}
    public record Location(
            double lat,
            double lng
    ) {}
    public record Viewport(
            Location northeast,
            Location southwest
    ) {}
    public record PlusCode(
            String compound_code,
            String global_code
    ) {}
}
