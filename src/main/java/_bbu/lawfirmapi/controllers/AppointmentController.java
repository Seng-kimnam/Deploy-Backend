package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.appointment.request.AppointmentFilterRequest;
import _bbu.lawfirmapi.models.DTO.appointment.request.AppointmentRequest;
import _bbu.lawfirmapi.models.DTO.appointment.response.AppointmentResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Appointment;
import _bbu.lawfirmapi.repositories.AppointmentRepository;
import _bbu.lawfirmapi.services.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController extends BaseResponse {

    private final AppointmentService appointmentService;


    @GetMapping
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> fetchAllAppointment(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "appointmentId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<AppointmentResponse> appointmentList = appointmentService.getAllAppointments(pageable , page);
        return responseEntity(true ,
                "Get appointment List"  ,
                HttpStatus.OK ,
                appointmentList);

    }


    @GetMapping("/filter-appointment")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> fetchFilteredAppointment(
            @ModelAttribute AppointmentFilterRequest requestFilter,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "appointmentId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
            ){

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<AppointmentResponse> appointmentList = appointmentService.getFilterAppointment(requestFilter , pageable , page  );
        return responseEntity(true ,
                "Filter appointment with " + requestFilter + " successfully" ,
                HttpStatus.OK ,
                appointmentList);
    }
    @GetMapping("/search-appointment")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> fetchAppointmentByKeyword(
            @RequestParam String keyword ,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "appointmentId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
            ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<AppointmentResponse> appointmentList = appointmentService.searchAllAppointmentBy(pageable , page , keyword);
        return responseEntity(true ,
                "Search appointment list by " + keyword + " successfully." ,
                HttpStatus.OK ,
                appointmentList);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> fetchAppointmentById (@PathVariable Long appointmentId){
        return responseEntity(true ,
                "Get appointment with id " + appointmentId +" successfully",
                HttpStatus.ACCEPTED ,
                appointmentService.getAppointmentById(appointmentId));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createNewAppointment(@RequestBody AppointmentRequest appointmentRequest){

        return responseEntity(true ,
                "Create new appointment successfully" ,
                HttpStatus.CREATED ,
                appointmentService.createNewAppointment(appointmentRequest));
    }
    @PutMapping(value = "/{appointmentId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateExistingAppointmentById( @PathVariable Long appointmentId,  @RequestBody AppointmentRequest appointmentRequest){

        return responseEntity(true ,
                "Update existing appointment successfully" ,
                HttpStatus.CREATED ,
                appointmentService.modifiedAppointmentById(appointmentId,appointmentRequest) );
    }
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointmentById(@PathVariable Long appointmentId){

        return responseEntity(true ,
                "Delete appointment successfully." ,
                HttpStatus.CREATED ,
                appointmentService.removeAppointmentById(appointmentId) );
    }
}
