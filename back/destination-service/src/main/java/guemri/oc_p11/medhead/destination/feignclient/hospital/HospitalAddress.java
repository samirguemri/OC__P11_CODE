package guemri.oc_p11.medhead.destination.feignclient.hospital;

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
