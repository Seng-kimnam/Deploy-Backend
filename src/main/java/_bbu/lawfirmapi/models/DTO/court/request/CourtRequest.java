package _bbu.lawfirmapi.models.DTO.court.request;


import _bbu.lawfirmapi.models.Entity.Court;
import _bbu.lawfirmapi.models.Enumerations.CourtType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonPropertyOrder({"courtId", "courtName", "courtType", "location", "contactNumber", "createdAt", "updatedAt"})
public class CourtRequest {

    private String courtName;

    private CourtType courtType ;

    private String location ;

    private String contactNumber ;

    public Court toEntity(){
        return new Court(null , this.courtName ,this.courtType , this.location , this.contactNumber );
    }
}
