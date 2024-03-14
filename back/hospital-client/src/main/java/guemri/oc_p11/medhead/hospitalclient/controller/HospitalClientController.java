package guemri.oc_p11.medhead.hospitalclient.controller;

import guemri.oc_p11.medhead.hospitalclient.dao.HospitalResponse;
import guemri.oc_p11.medhead.hospitalclient.service.hospitalClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/hospitalclient")
public record HospitalClientController(hospitalClientService hospitalClientService) {

    @GetMapping("/all")
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        return ResponseEntity.status(HttpStatus.OK).body(hospitalClientService.getAllHospitals());
    }
}
