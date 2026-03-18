package _bbu.lawfirmapi.models.DTO.verification.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    private LocalDateTime expireDateTime;
    private String verifiedCode;
    private Boolean isVerified;
    private String email;
    private Long appUserId;
}
