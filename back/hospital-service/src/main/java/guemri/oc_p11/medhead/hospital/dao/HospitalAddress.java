package guemri.oc_p11.medhead.hospital.dao;

/*import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;*/

import lombok.Builder;

@Builder
public record HospitalAddress(
        String numberAndStreet,
        String locality,
        String postCode,
        String country
        ) {

        @Override
        public String toString() {
                return String.join(" ", numberAndStreet, locality, postCode, country);
        }
}
