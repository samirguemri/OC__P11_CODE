package guemri.oc_p11.medhead.hospital.repository;

import guemri.oc_p11.medhead.hospital.entity.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Optional<Hospital> findHospitalByHospitalName(String name);
}
