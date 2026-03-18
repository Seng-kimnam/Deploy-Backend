package _bbu.lawfirmapi.models.DTO.service.request;

import _bbu.lawfirmapi.models.Entity.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    private String serviceName;
    private String description;
    private Float basePrice;
    private Integer expertiseId;

    public Service toEntity(){
        return new Service(
                null ,
                this.serviceName,
                this.description,
                this.basePrice,
                this.expertiseId
        );
    }
}
