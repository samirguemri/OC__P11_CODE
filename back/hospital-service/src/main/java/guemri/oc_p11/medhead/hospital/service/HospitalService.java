package guemri.oc_p11.medhead.hospital.service;

import com.google.gson.Gson;
import guemri.oc_p11.medhead.hospital.feignclient.geocodingapi.GeocodingAddressResponse;
import guemri.oc_p11.medhead.hospital.feignclient.geocodingapi.GoogleMapsGeocodingClient;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityResponse;
import guemri.oc_p11.medhead.hospital.entity.Hospital;
import guemri.oc_p11.medhead.hospital.dao.HospitalRequest;
import guemri.oc_p11.medhead.hospital.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospital.repository.HospitalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import guemri.oc_p11.medhead.hospital.feignclient.speciality.SpecialityClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public record HospitalService(
        HospitalRepository hospitalRepository,
        SpecialityClient specialityClient,
        GoogleMapsGeocodingClient geocodingClient,
        Gson gson
        ) {

    private static final String googleMapsApiKey = "AIzaSyCDE4qwwbeKMVOsBPtE4lsNjYrRohyIATQ";

    public HospitalResponse registerHospital(HospitalRequest request) {

        // request the specialities from speciality-service
        List<SpecialityResponse> specialityResponseList = specialityClient.getSpecialitiesFromList(request.specialities()).getBody();

        // request the address geocode from google-maps-geocoding
        String encodedAddress = URLEncoder.encode(request.hospitalAddress().toString(), StandardCharsets.UTF_8);

        String jsonResponse = geocodingClient.calculateGeocode(encodedAddress, googleMapsApiKey);

        //Type listType = new TypeToken<List<GeocodingAddressResponse.Result>>() {}.getType();
        log.info("Geocoding Response : {}", jsonResponse);
        GeocodingAddressResponse response = gson.fromJson(jsonResponse, GeocodingAddressResponse.class);
        GeocodingAddressResponse.Result result = response.results().get(0);

        Hospital newHospital = hospitalBuilder(request, result.geometry().location(), specialityResponseList);
        newHospital = hospitalRepository.save(newHospital);

        return hospitalResponseBuilder(Optional.of(newHospital));
    }

    public HospitalResponse getHospitalById(String id) {

        Optional<Hospital> optionalHospital = hospitalRepository.findById(id);
        return hospitalResponseBuilder(optionalHospital);
    }

    public HospitalResponse getHospitalByName(String name) {
        Optional<Hospital> optionalHospital = hospitalRepository.findHospitalByHospitalName(name);
        return hospitalResponseBuilder(optionalHospital);
    }

    public List<HospitalResponse> getHospitalsBySpeciality(String speciality) {
        return getAllHospitals().stream()
                .filter(hospitalResponse -> haveSpeciality(hospitalResponse, speciality))
                .collect(Collectors.toList());
    }

    public List<HospitalResponse> getAllHospitals() {

        List<Hospital> hospitals = hospitalRepository.findAll();
        return hospitals.stream()
                .map(hospital -> hospitalResponseBuilder(Optional.of(hospital)))
                .collect(Collectors.toList());
    }

    public HospitalResponse updateHospitalName(HospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .hospitalRef(request.hospitalRef())
                .hospitalName(request.hospitalName())
                .build();

        Hospital updatedHospital = hospitalRepository.save(hospital);

        return HospitalResponse.builder()
                .hospitalRef(updatedHospital.getHospitalRef())
                .hospitalName(updatedHospital.getHospitalName())
                .hospitalAddress(updatedHospital.getHospitalAddress())
                .availableBeds(updatedHospital.getAvailableBeds())
                .specialities(updatedHospital.getSpecialities())
                .build();
    }

    public HospitalResponse updateHospitalAvailableBeds(HospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .hospitalRef(request.hospitalRef())
                .availableBeds(request.availableBeds())
                .build();

        Hospital updatedHospital = hospitalRepository.save(hospital);

        return HospitalResponse.builder()
                .hospitalRef(updatedHospital.getHospitalRef())
                .hospitalName(updatedHospital.getHospitalName())
                .hospitalAddress(updatedHospital.getHospitalAddress())
                .availableBeds(updatedHospital.getAvailableBeds())
                .specialities(updatedHospital.getSpecialities())
                .build();
    }

    public void deleteHospitalById(String id) {
        hospitalRepository.deleteById(id);
    }

    private Hospital hospitalBuilder(HospitalRequest request, GeocodingAddressResponse.Location location, List<SpecialityResponse> specialityResponseList) {
        return Hospital.builder()
                .hospitalName(request.hospitalName())
                .hospitalAddress(request.hospitalAddress())
                .latitude(location.lat())
                .longitude(location.lng())
                .availableBeds(request.availableBeds())
                .specialities(specialityResponseList)
                .build();
    }

    private HospitalResponse hospitalResponseBuilder(Optional<Hospital> optionalHospital) {
        Hospital hospital = null;
        try {
            hospital = optionalHospital.orElseThrow();
        } catch (NoSuchElementException e) {
            //todo handle exception
        }
        assert hospital != null;
        return HospitalResponse.builder()
                .hospitalRef(hospital.getHospitalRef())
                .hospitalName(hospital.getHospitalName())
                .hospitalAddress(hospital.getHospitalAddress())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .availableBeds(hospital.getAvailableBeds())
                .specialities(hospital.getSpecialities())
                .build();
    }

    private boolean haveSpeciality(HospitalResponse hospitalResponse, String speciality) {
        return !hospitalResponse.specialities().stream().filter(specialityResponse -> specialityResponse.specialityName().equals(speciality)).toList().isEmpty();
    }

}
