package _bbu.lawfirmapi.models.DTO.expertise.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpertiseResponse {
    private Integer expertiseId;
    private String expertName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
