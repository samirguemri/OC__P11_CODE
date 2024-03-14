package guemri.oc_p11.medhead.hospitalclient.repository;

import guemri.oc_p11.medhead.hospitalclient.entity.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HospitalClientRepository extends MongoRepository<Hospital,String> {
}
