package _bbu.lawfirmapi.models.DTO.expertise.request;

import _bbu.lawfirmapi.models.Entity.Expertise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpertiseRequest {
    private String expertName;

    public Expertise toEntity(){
        return new Expertise(null , this.expertName);
    }
}
