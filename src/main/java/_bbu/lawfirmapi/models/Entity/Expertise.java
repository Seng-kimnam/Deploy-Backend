package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.expertise.response.ExpertiseResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expertises")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"expertiseId", "expertName", "createdAt", "updatedAt"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Expertise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "expertise_id")
    private Integer expertiseId;

    @Column(name = "expert_name")
    @ToString.Include
    private String expertName;

    @ManyToMany(mappedBy = "expertises")
    @JsonIgnore
//    @ToString.Exclude
    private Set<AppUser> lawyerProfiles;

    @OneToMany(mappedBy = "expertise", cascade = CascadeType.ALL)
    @JsonIgnore
//    @ToString.Exclude
    private Set<Service> serviceSet;

    public Expertise(Object o, String expertName) {
    }

    public ExpertiseResponse toResponse() {
        return new ExpertiseResponse(
                this.expertiseId,
                this.expertName,
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }
}
