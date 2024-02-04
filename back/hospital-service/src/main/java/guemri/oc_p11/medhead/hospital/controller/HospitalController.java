package guemri.oc_p11.medhead.hospital.controller;

import guemri.oc_p11.medhead.hospital.dao.HospitalRequest;
import guemri.oc_p11.medhead.hospital.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospital.service.HospitalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/hospitals")
public record HospitalController(HospitalService hospitalService) {

    @PostMapping("/add")
    public ResponseEntity<HospitalResponse> registerHospital(@RequestBody HospitalRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.registerHospital(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getHospitalById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<HospitalResponse> getHospitalByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getHospitalByName(name));
    }

    @GetMapping("/speciality/{speciality}")
    public ResponseEntity<List<HospitalResponse>> getHospitalsBySpeciality(@PathVariable String speciality) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getHospitalsBySpeciality(speciality));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.getAllHospitals());
    }

    @PostMapping("/update/name")
    public ResponseEntity<HospitalResponse> updateHospitalName(@RequestBody HospitalRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.updateHospitalName(request));
    }

    @PostMapping("/update/beds")
    public ResponseEntity<HospitalResponse> updateHospitalAvailableBeds(@RequestBody HospitalRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalService.updateHospitalAvailableBeds(request));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteHospitalById(@PathVariable String id) {
        hospitalService.deleteHospitalById(id);
        return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
    }
}
