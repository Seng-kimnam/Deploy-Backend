package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.role.response.RoleResponse;
import _bbu.lawfirmapi.models.Entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE LOWER(r.roleName) LIKE LOWER(CONCAT('%',:keyword ,'%'))")
    Role searchByRoleName (@Param("keyword") String keyword);
}