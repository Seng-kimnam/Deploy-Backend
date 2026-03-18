package _bbu.lawfirmapi.models.DTO.role.request;

import _bbu.lawfirmapi.models.Entity.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String roleName;

    public Role toEntity(){
        return new Role(null , this.roleName);
    }
}
