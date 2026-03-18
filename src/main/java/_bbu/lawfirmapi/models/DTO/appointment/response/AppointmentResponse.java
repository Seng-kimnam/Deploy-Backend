package _bbu.lawfirmapi.models.DTO.appointment.response;

import _bbu.lawfirmapi.models.Entity.*;
import _bbu.lawfirmapi.models.Enumerations.AppointmentStatus;
import _bbu.lawfirmapi.models.Enumerations.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;
    private Task task;
    private String appointmentDate;
    private String appointmentTime;
    private MeetingType meetingType;
    private String location;
    private String purpose;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
