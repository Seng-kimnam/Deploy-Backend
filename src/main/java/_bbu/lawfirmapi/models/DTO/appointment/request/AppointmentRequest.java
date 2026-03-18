package _bbu.lawfirmapi.models.DTO.appointment.request;

import _bbu.lawfirmapi.models.Entity.*;
import _bbu.lawfirmapi.models.Enumerations.AppointmentStatus;
import _bbu.lawfirmapi.models.Enumerations.MeetingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppointmentRequest {

    private Long taskId;
    private String appointmentDate;
    private String appointmentTime;
    private MeetingType meetingType;
    private String location;
    private String purpose;
    private AppointmentStatus status;

    public Appointment toEntity(){
        return new Appointment(
                null,
                taskId,
                appointmentDate,
                appointmentTime,
                meetingType,
                location,
                purpose,
                status
        );
    }
}
