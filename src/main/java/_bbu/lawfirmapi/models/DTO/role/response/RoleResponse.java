package _bbu.lawfirmapi.models.DTO.role.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Integer roleId;
    private String roleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
