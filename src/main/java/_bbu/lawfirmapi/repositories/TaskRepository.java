package _bbu.lawfirmapi.repositories;


import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.models.Enumerations.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    boolean existsByLawyer_AppUserIdAndLegalCase_CaseId(Long lawyerAppUserId, Long legalCaseCaseId);
    Page<Task> findTaskByLawyerEmail(Pageable pageable, String lawyer_email);

    @Query("SELECT t FROM Task t WHERE t.lawyer.appUserId = :lawyerId")
    List<Task> findByLawyerAppUserId(Long lawyerId);

    @Query("SELECT t FROM Task t WHERE t.lawyer.appUserId = :lawyerId")
    Page<Task> findByLawyerAppUserId(Long lawyerId, Pageable pageable);

    List<Task> findTaskByStatus(TaskStatus status);
}
