package _bbu.lawfirmapi.services.role.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.role.request.RoleRequest;
import _bbu.lawfirmapi.models.DTO.role.response.RoleResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Role;
import _bbu.lawfirmapi.repositories.RoleRepository;
import _bbu.lawfirmapi.services.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
//    private final RoleWithMyBatis roleWithMyBatis;
    // get all role method

    public AppUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            return null;
        }
        return (AppUser) authentication.getPrincipal();
    }
    @Override
    public List<Role> getAllRoles(){

//        System.out.println(roleRepository.findAll().isEmpty());
        return Optional.of(roleRepository.findAll())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new NotFoundException("None any role founded"));
    }
    @Override
    public Role findRoleByKeyword(String keyword){

        if(keyword.isBlank()){
            throw new NotFoundException("Cannot search your empty word");
        }
        return roleRepository.searchByRoleName(keyword.trim());
    }
    @Override
    public Role findRoleByRoleId(Integer roleId){
       return roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException(" Role id not found"));
    }
    @Override
    public RoleResponse createNewRoleList(RoleRequest newRoleRequest){
        Role newRole = newRoleRequest.toEntity();
        newRole.setRoleName(newRoleRequest.getRoleName());
        return roleRepository.save(newRole).toResponse();
    }
    @Override
    public RoleResponse updateRoleById(Integer roleId  , RoleRequest updateRole){
        Role updateNewRole = roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("role Id "  + roleId + "not found"));
        updateNewRole.setRoleName(updateRole.getRoleName());

        return roleRepository.save(updateNewRole).toResponse();
    }
    @Override
    public Void removeRoleById(Integer roleId){
        if(roleRepository.findById(roleId).isEmpty()){
//            System.out.println("role deleted" + roleRepository.findById(roleId));
            roleRepository.deleteById(roleId);
        }
        return null;
    }

}
