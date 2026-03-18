package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.appointment.response.AppointmentResponse;
import _bbu.lawfirmapi.models.Enumerations.AppointmentStatus;
import _bbu.lawfirmapi.models.Enumerations.MeetingType;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.annotations.One;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @OneToOne(fetch = FetchType.LAZY)  // "appointment" matches Case.appointment
    @JoinColumn(name = "task_id" , referencedColumnName = "task_id")
    @ToString.Exclude
    @JsonIgnore
    private Task task;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "appuser_id" , referencedColumnName = "appuser_id")
//    private AppUser lawyer;

    @Column(name = "appointment_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String appointmentDate;

    @Column(name = "appointment_time" , columnDefinition = "TEXT" )
    @JsonFormat(pattern = "HH:mm")
    private String appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type")
    private MeetingType meetingType;

    @Column(name = "location")
    private String location;

    @Column(name = "purpose" , columnDefinition = "TEXT")
    private String purpose;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    public Appointment(Long o, Long taskId, String appointmentDate, String appointmentTime, MeetingType meetingType, String location, String purpose, AppointmentStatus status) {
    }


    public AppointmentResponse toResponse(){

        return new AppointmentResponse(
                appointmentId,
                task,
                appointmentDate,
                appointmentTime,
                meetingType,
                location,
                purpose,
                status,
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }
}
