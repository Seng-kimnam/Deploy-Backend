package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.appointment.response.AppointmentResponse;
import _bbu.lawfirmapi.models.Entity.Appointment;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppointmentRepository  extends JpaRepository<Appointment , Long> , JpaSpecificationExecutor<Appointment>{

    @Query("SELECT a FROM Appointment  a JOIN FETCH a.task WHERE a.task.lawyer.email = :email")
    Page<Appointment> findAllWithAppUser(Pageable pageable , @Param("email") String email);
//    @Query("SELECT a FROM Appointment  a JOIN FETCH a.task WHERE a.task.lawyer.email = :email LI")
//    Page<Appointment> findAllWithAppUser(Pageable pageable , @Param("email") String email , @Param("keyword") String keyword);

    @Query("SELECT a FROM Appointment  a JOIN FETCH a.task WHERE a.appointmentId = :appointmentId AND a.task.lawyer.email = :email")
    Optional<Appointment> findAppointmentByAppointmentId(@Param("appointmentId") Long appointmentId , @Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "JOIN a.task t " +
           "WHERE t.lawyer.appUserId = :lawyerId " +
           "AND a.appointmentDate = :appointmentDate " +
           "AND a.appointmentTime = :appointmentTime")
    boolean existsByLawyerAndDateAndTime(
            @Param("lawyerId") Long lawyerId,
            @Param("appointmentDate") String appointmentDate,
            @Param("appointmentTime") String appointmentTime);

}
