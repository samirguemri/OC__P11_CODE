package guemri.oc_p11.medhead.speciality.controller;

import guemri.oc_p11.medhead.speciality.dao.SpecialityRequest;
import guemri.oc_p11.medhead.speciality.dao.SpecialityResponse;
import guemri.oc_p11.medhead.speciality.dao.SpecialityGroupResponse;
import guemri.oc_p11.medhead.speciality.service.SpecialityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("api/v1/specialities")
public record SpecialityController(SpecialityService specialityService) {

    @PostMapping("/post")
    public ResponseEntity<SpecialityResponse> insertSpeciality(@RequestBody SpecialityRequest request){
        SpecialityResponse response = specialityService.insertSpeciality(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/postAll")
    public ResponseEntity<List<SpecialityResponse>> insertBulkSpeciality(@RequestBody List<SpecialityRequest> requests){
        List<SpecialityResponse> responses = specialityService.insertBulkSpeciality(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/get/speciality/code/{code}")
    public ResponseEntity<SpecialityResponse> getSpecialityById(@PathVariable String code){
        try {
            SpecialityResponse response = specialityService.getSpecialityByCode(code);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get/speciality/name/{name}")
    public ResponseEntity<SpecialityResponse> getSpecialityByName(@PathVariable String name){
        try {
            SpecialityResponse response = specialityService.getSpecialityByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get/list")
    public ResponseEntity<List<SpecialityResponse>> getSpecialitiesFromList(@RequestParam List<String> speciality){
        try {
            List<SpecialityResponse> responses = specialityService.getSpecialitiesFromList(speciality);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<SpecialityResponse>> getAllSpecialities(){
        List<SpecialityResponse> responses = specialityService.getAllSpecialities();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/get/group/{groupName}")
    public ResponseEntity<SpecialityGroupResponse> getSpecialitiesByGroupName(@PathVariable String groupName){
        try {
            SpecialityGroupResponse response = specialityService.getSpecialitiesByGroupName(groupName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // todo : add update & delete requests

}
