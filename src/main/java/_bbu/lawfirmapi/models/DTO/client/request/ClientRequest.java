package _bbu.lawfirmapi.models.DTO.client.request;

import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.Enumerations.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Client creation request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {

    private String clientName;
    private String email;

    @Schema(
            description = "Client status",
            example = "PENDING",
            allowableValues = {"PENDING","IN_PROGRESS","DONE"}
    )
    private ClientStatus status;

    private String phoneNumber;
    private String address;
    private String complaint;
    private String clientImage;
    private String feedBack;

    public Client toEntity(){
        return new Client(
                null,
                this.clientName,
                this.email,
                this.status,
                this.phoneNumber,
                this.address,
                this.complaint,
                this.feedBack,
                this.clientImage
        );
    }
}
