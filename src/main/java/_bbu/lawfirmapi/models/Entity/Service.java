package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.service.response.ServiceResponse;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "services")
@JsonPropertyOrder({"serviceId" , "serviceName" , "description" ,"basePrice" , "createdAt" , "updatedAt"})
public class Service extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "description" , columnDefinition = "TEXT")
    private String description;
    @Column(name = "base_price")
    private Float basePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expertise_id" , referencedColumnName = "expertise_id")
    @JsonIgnore
    private Expertise expertise;

    public Service(Long serviceId, String serviceName, String description, Float basePrice, Integer expertiseId) {
    }

    public ServiceResponse toResponse(){
        return new ServiceResponse(
          this.serviceId,
          this.serviceName,
          this.description,
          this.basePrice,
          this.expertise.getExpertName(),
          this.getExpertise().getExpertiseId()
        );
    }

}
