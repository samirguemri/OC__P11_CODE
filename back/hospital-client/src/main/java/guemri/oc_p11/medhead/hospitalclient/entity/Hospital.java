package guemri.oc_p11.medhead.hospitalclient.entity;

import guemri.oc_p11.medhead.hospitalclient.dao.HospitalAddress;
import guemri.oc_p11.medhead.hospitalclient.dao.SpecialityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Hospital {
    @Id
    String hospitalRef;
    String hospitalName;
    HospitalAddress hospitalAddress;
    double latitude;
    double longitude;
    int availableBeds;
    List<SpecialityResponse> specialities;
}
