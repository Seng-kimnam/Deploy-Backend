package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.court.response.CourtResponse;
import _bbu.lawfirmapi.models.Enumerations.CourtType;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courts")
@JsonPropertyOrder({"courtId", "courtName", "courtType", "location", "contactNumber", "createdAt", "updatedAt"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Court extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Long courtId;

    @Column(name = "court_name")
    private String courtName;

    @Enumerated(EnumType.STRING)
    @Column(name =  "court_type" , columnDefinition = "VARCHAR(100)")
    private CourtType courtType ;

    @Column(name = "location")

    private String location ;

    @Column(name = "contact_number")
    private String contactNumber ;

    @OneToMany(mappedBy = "court" , cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore  // Add this
    private List<Case> cases;


    public Court(Object o, String courtName, CourtType courtType, String location, String contactNumber) {
    }


    public CourtResponse  toResponse(){
       return new CourtResponse(this.courtId , this.courtName , this.courtType ,  this.location , this.contactNumber);
    }
}
