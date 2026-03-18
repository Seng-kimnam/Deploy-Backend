package _bbu.lawfirmapi.models.DTO.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private Long serviceId;
    private String serviceName;
    private String description;
    private Float basePrice;
    private String expertiseName;
    private Integer expertiseId;

}
