package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.service.response.ServiceResponse;
import _bbu.lawfirmapi.models.Entity.Service;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRepository extends JpaRepository<Service , Long> {

//    @Query("""
//    SELECT ser FROM Service ser WHERE LOWER(ser.serviceName) LIKE LOWER(CONCAT('%',:keyword,'%'))
//         OR LOWER(ser.basePrice) LIKE LOWER(CONCAT('%', :keyword , '%'))
//         OR LOWER(ser.description) LIKE LOWER(CONCAT('' , :keyword , '%' ))
//    """)
//    Page<ServiceResponse> searchServiceByKeyword(@Param("keyword") String keyword);
}
