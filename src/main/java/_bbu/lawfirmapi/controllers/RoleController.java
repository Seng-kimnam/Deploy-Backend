package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.role.request.RoleRequest;
import _bbu.lawfirmapi.models.DTO.role.response.RoleResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Role;
import _bbu.lawfirmapi.repositories.RoleRepository;
import _bbu.lawfirmapi.services.role.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseResponse {
    private final RoleService  roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles(){
        return responseEntity(true ,
                "Getting all role" ,
                        HttpStatus.ACCEPTED,
                        roleService.getAllRoles());
    }
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Integer roleId){
        return responseEntity(true,
                "Getting Role " + roleService.findRoleByRoleId(roleId).getRoleName() + " success" ,
                HttpStatus.OK,
                roleService.findRoleByRoleId(roleId));
    }
    @GetMapping("/search-role")
    public ResponseEntity<ApiResponse<Role>> getRoleByKeyWord(@RequestParam String keyword){
        return responseEntity(true,
                "Getting Role with keyword" + keyword  + " successfully" ,
                HttpStatus.OK ,
                roleService.findRoleByKeyword(keyword));
    }
//    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createNewRole(@RequestBody RoleRequest roleRequest){
        return responseEntity(true , "Created new role success" , HttpStatus.CREATED ,roleService.createNewRoleList(roleRequest));
    }

//    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateNewRole (@PathVariable Integer roleId , @RequestBody RoleRequest newRoleRequest) {

        return responseEntity(true, "update role success", HttpStatus.ACCEPTED, roleService.updateRoleById(roleId, newRoleRequest));
    }
//    @SecurityRequirement(name = "bearerAuth")

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRoleById(@PathVariable Integer roleId) {
        return responseEntity(true ,
                "Delete role " + roleService.findRoleByRoleId(roleId) + " successfully" ,
                HttpStatus.ACCEPTED,
                roleService.removeRoleById(roleId)

                );
    }


}
