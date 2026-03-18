package _bbu.lawfirmapi.models.DTO.appointment.request;

import _bbu.lawfirmapi.models.Enumerations.AppointmentStatus;
import _bbu.lawfirmapi.models.Enumerations.MeetingType;
import lombok.Data;

@Data
public class AppointmentFilterRequest {
    private AppointmentStatus status;
    private MeetingType meetingType;
    private String appointmentDate;  // yyyy-MM-dd
    private String location;
    private String clientName;
}