package _bbu.lawfirmapi.models.Enumerations;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Client status values")
public enum ClientStatus {

    @Schema(description = "Client has submitted but not processed yet")
    PENDING,

    @Schema(description = "Client has rejected")
    REJECTED,
    @Schema(description = "Client has been approved")
    APPROVED,

    @Schema(description = "Client's case is in progress")
    IN_PROGRESS,

    @Schema(description = "Client case completed")
    DONE
}
