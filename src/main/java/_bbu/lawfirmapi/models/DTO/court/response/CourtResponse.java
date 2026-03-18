package _bbu.lawfirmapi.models.DTO.court.response;

import _bbu.lawfirmapi.models.Enumerations.CourtType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourtResponse {
    private Long courtId;

    private String courtName;

    private CourtType courtType ;

    private String location ;

    private String contactNumber ;
}
