package guemri.oc_p11.medhead.speciality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Speciality {
    @Id
    String code;
    @Indexed(unique = true)
    String specialityName;
    @Indexed(unique = true)
    String specialityGroup;
}
