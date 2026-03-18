package _bbu.lawfirmapi.models.DTO.cases.response;

import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.Entity.Court;
import _bbu.lawfirmapi.models.Enumerations.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseResponse {
    private Long caseId;
    private Client client;
    private Court court;
    private String title;
    private String description;
    private CaseStatus status;
    private LocalDateTime statedDate;
    private LocalDateTime endedDate;
}
