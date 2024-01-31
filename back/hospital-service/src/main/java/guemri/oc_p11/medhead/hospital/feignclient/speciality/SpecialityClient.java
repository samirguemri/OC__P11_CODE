package guemri.oc_p11.medhead.hospital.feignclient.speciality;

import guemri.oc_p11.medhead.hospital.configuration.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "speciality-service",
        url = "http://localhost:9000",
        path = "api/v1/specialities",
        configuration = ClientConfiguration.class
)
public interface SpecialityClient {

    @PostMapping("/post")
    ResponseEntity<SpecialityResponse> insertSpeciality(@RequestBody SpecialityRequest request);

    @PostMapping("/postAll")
    ResponseEntity<List<SpecialityResponse>> insertBulkSpeciality(@RequestBody List<SpecialityRequest> requests);

    @GetMapping("/get/speciality/code/{code}")
    ResponseEntity<SpecialityResponse> getSpecialityById(@PathVariable("code") String code);

    @GetMapping("/get/speciality/name/{name}")
    ResponseEntity<SpecialityResponse> getSpecialityByName(@PathVariable("name") String name);

    @GetMapping("/get/list")
    ResponseEntity<List<SpecialityResponse>> getSpecialitiesFromList(@RequestParam("speciality") List<String> speciality);

    @GetMapping("/get/all")
    ResponseEntity<List<SpecialityResponse>> getAllSpecialities();

    @GetMapping("/get/group/{groupName}")
    ResponseEntity<SpecialityGroupResponse> getSpecialitiesByGroupName(@PathVariable("groupName") String groupName);
}
