package guemri.oc_p11.medhead.speciality.repository;

import guemri.oc_p11.medhead.speciality.entity.Speciality;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository extends MongoRepository<Speciality, String> {
    Optional<Speciality> findSpecialityByCode(String code);
    Optional<Speciality> findSpecialityBySpecialityName(String name);
    Optional<List<Speciality>> findSpecialitiesBySpecialityGroup(String groupName);
}
