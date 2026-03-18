package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.Entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {

    boolean existsByCategoryName(
            @Param("categoryName") String categoryName
    );
}
