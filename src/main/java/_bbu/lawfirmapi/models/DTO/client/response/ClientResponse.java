package _bbu.lawfirmapi.models.DTO.client.response;

import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Enumerations.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientResponse {

    private Long  clientId ;
    private String clientName ;
    private String email ;
    private ClientStatus status;
    private String phoneNumber;
    private String address;
    private String complaint ;
    private String clientImage;
    private String feedBack;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
