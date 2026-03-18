package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.Entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
//    @Query("SELECT DISTINCT c FROM Case c JOIN FETCH c.client  JOIN FETCH c.court ")
//    List<Case> findAllWithCases();

    boolean existsByClient_ClientIdAndCourt_CourtIdAndTitleAndStartDate(
            Long clientId,
            Long courtId,
            String title,
            LocalDateTime startDate
    );

    @Query("SELECT c FROM Case c WHERE c.createdAt IS NOT NULL AND EXTRACT(YEAR FROM c.createdAt) = :year")
    Page<Case> findByYear(@Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM Case c WHERE c.createdAt IS NOT NULL AND EXTRACT(YEAR FROM c.createdAt) = :year AND EXTRACT(MONTH FROM c.createdAt) = :month")
    Page<Case> findByYearAndMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

    @Query("SELECT c FROM Case c WHERE c.createdAt IS NOT NULL AND EXTRACT(YEAR FROM c.createdAt) = :year AND EXTRACT(MONTH FROM c.createdAt) = :month AND EXTRACT(DAY FROM c.createdAt) = :day")
    Page<Case> findByYearAndMonthAndDay(@Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageable);

}
