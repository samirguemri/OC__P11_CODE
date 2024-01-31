package guemri.oc_p11.medhead.destination.controller;

import guemri.oc_p11.medhead.destination.dto.DestinationRequest;
import guemri.oc_p11.medhead.destination.service.DestinationService;
import guemri.oc_p11.medhead.destination.feignclient.hospital.HospitalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/destination")
public record DestinationController(DestinationService destinationService) {

    @PostMapping("/search")
    public ResponseEntity<HospitalResponse> searchDestination(@RequestBody DestinationRequest destinationRequest) throws InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(destinationService.searchDestination(destinationRequest));
    }

    @GetMapping("/test")
    public String test(){
        return "hello from destination service";
    }
}
