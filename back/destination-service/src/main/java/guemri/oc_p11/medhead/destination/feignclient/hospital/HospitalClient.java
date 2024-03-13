package guemri.oc_p11.medhead.destination.feignclient.hospital;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "hospital-client",
        url = "${spring.cloud.openfeign.client.config.hospitalClient.url}",
        path = "api/v1/hospitals"
)
public interface HospitalClient {

    @GetMapping("/speciality/{speciality}")
    ResponseEntity<List<HospitalResponse>> getHospitalsBySpeciality(@PathVariable("speciality") String speciality);

    /*
    @PostMapping("/add")
    ResponseEntity<HospitalResponse> registerHospital(@RequestBody HospitalRequest request);

    @GetMapping("/{id}")
    ResponseEntity<HospitalResponse> getHospitalById(@PathVariable("id") String id);

    @GetMapping("/{name}")
    ResponseEntity<HospitalResponse> getHospitalByName(@PathVariable("name") String name);

    @GetMapping("/all")
    ResponseEntity<List<HospitalResponse>> getAllHospitals();

    @PostMapping("/update/name")
    ResponseEntity<HospitalResponse> updateHospitalName(@RequestBody HospitalRequest request);

    @PostMapping("/update/beds")
    ResponseEntity<HospitalResponse> updateHospitalAvailableBeds(@RequestBody HospitalRequest request);

    @GetMapping("/delete/{id}")
    ResponseEntity<String> deleteHospitalById(@PathVariable("id") String id);
    */
}
