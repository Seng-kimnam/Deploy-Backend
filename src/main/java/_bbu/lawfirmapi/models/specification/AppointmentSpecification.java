package _bbu.lawfirmapi.models.specification;

import _bbu.lawfirmapi.models.DTO.appointment.request.AppointmentFilterRequest;
import _bbu.lawfirmapi.models.Entity.Appointment;
import _bbu.lawfirmapi.models.Enumerations.AppointmentStatus;
import _bbu.lawfirmapi.models.Enumerations.MeetingType;
import _bbu.lawfirmapi.repositories.AppointmentRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
    public class AppointmentSpecification {

    public static Specification<Appointment> withFilters(AppointmentFilterRequest filter) {
        Specification<Appointment> spec = Specification.not(null); // start with no-op / match all

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getMeetingType() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("meetingType"), filter.getMeetingType()));
        }
        if (filter.getAppointmentDate() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("appointmentDate"), filter.getAppointmentDate()));
        }
        if (filter.getLocation() != null) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("location")), "%" + filter.getLocation().toLowerCase() + "%"
            ));
        }
        if (filter.getClientName() != null) {
            spec = spec.and((root, query, cb) -> cb.like(
                    cb.lower(root.get("clientName")), "%" + filter.getClientName().toLowerCase() + "%"
            ));
        }
        return spec;
    }

    public static Specification<Appointment> search(
            String keyword

//            boolean isAdmin
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // keyword search
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("purpose")), like),
                                cb.like(cb.lower(root.get("location")), like)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
//public class AppointmentSpecification {
//    private final AppointmentRepository appointmentRepository;


//    public static Specification<Appointment> filter(
//            String appointmentDate,
//            MeetingType meetingType,
//            String location,
//            AppointmentStatus status,
//            String keyword
//    ) {
//        return (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (appointmentDate != null && !appointmentDate.isEmpty()) {
//                predicates.add(cb.equal(root.get("appointmentDate"), appointmentDate));
//            }
//
//            if (meetingType != null) {
//                predicates.add(cb.equal(root.get("meetingType"), meetingType));
//            }
//
//            if (status != null) {
//                predicates.add(cb.equal(root.get("status"), status));
//            }
//
//            if (location != null && !location.isEmpty()) {
//                predicates.add(cb.like(
//                        cb.lower(root.get("location")),
//                        "%" + location.toLowerCase() + "%"
//                ));
//            }
//
//            // Search text (purpose OR location)
//            if (keyword != null && !keyword.isEmpty()) {
//                String like = "%" + keyword.toLowerCase() + "%";
//                predicates.add(
//                        cb.or(
//                                cb.like(cb.lower(root.get("purpose")), like),
//                                cb.like(cb.lower(root.get("location")), like)
//                        )
//                );
//            }
//
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//    }
//
//
//
//    public  List<Appointment> searchAllAppointment(
//            String appointmentDate,
//            MeetingType meetingType,
//            String location,
//            AppointmentStatus status,
//            String keyword
//    ) {
//        Specification<Appointment> spec =
//                AppointmentSpecification.filter(
//                        appointmentDate,
//                        meetingType,
//                        location,
//                        status,
//                        keyword
//                );
//
//        return appointmentRepository.findAll(spec);
//    }
//

//
//
//}
