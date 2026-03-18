package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.role.response.RoleResponse;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"roleId" , "roleName" , "createdAt"  , "updatedAt"})
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<AppUser> users;

    public Role(Integer roleId, String roleName) {
    }

    public RoleResponse toResponse(){
        return new RoleResponse(this.roleId , this.roleName , this.getCreatedAt() , this.getUpdatedAt() );
    }
}
