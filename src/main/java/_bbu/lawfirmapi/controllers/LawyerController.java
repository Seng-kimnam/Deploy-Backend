package _bbu.lawfirmapi.controllers;
import _bbu.lawfirmapi.models.DTO.appuser.request.AppUserRequest;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Appointment;
import _bbu.lawfirmapi.models.Entity.Task;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.services.admin.AdminService;
import _bbu.lawfirmapi.services.auth.AppUserService;
import _bbu.lawfirmapi.services.lawyer.LawyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lawyers")
public class LawyerController extends BaseResponse {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final AdminService adminService;
    private final LawyerService lawyerService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<AppUserResponse>>> getAllUser(){
        return responseEntity(true ,
                "Get all Lawyers successfully." ,
                HttpStatus.OK ,
                lawyerService.fetchAllLawyers());
    }


    @GetMapping("/{lawyerId}")
    public ResponseEntity<ApiResponse<AppUser>> fetchLawyerById(@PathVariable Long lawyerId){
        return responseEntity(true ,
                "Get lawyer with id " + lawyerId + " successfully.",
                HttpStatus.ACCEPTED,
                lawyerService.fetchLawyerById(lawyerId)
        );
    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/lawyer-profile")
    public ResponseEntity<ApiResponse<AppUserResponse>> fetchLawyerProfile(){
        return responseEntity(true ,
                "Get current lawyer profile successfully." ,
                HttpStatus.OK ,
                lawyerService.getCurrentLawyerProfile());
    }
    @GetMapping("/search-lawyer")
    public ResponseEntity<ApiResponse<List<AppUserResponse>>> searchLawyerByUserNamePhoneNumberEmail(
            @RequestParam(required = false) String keyword
    ) {

        return responseEntity(true,
                "Search laywer successfully.",
                HttpStatus.OK,
                lawyerService.findLawyerByUsernameORPhoneNumberOREmail(keyword ));
    }
    @GetMapping("/task-list-by-lawyer")
    public ResponseEntity<ApiResponse<Page<Task>>> fetchTaskByLawyer(
            @RequestParam String email,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "taskId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1 , size , sort);
        Page<Task> tasksList =lawyerService.getTaskByLawyerEmail(pageable , page , email);
        return responseEntity(true,
                "Getting task list successfully.",
                HttpStatus.OK,
                tasksList);
    }

//        @PutMapping("/reset-password")
//    @Operation(summary = "Reset Password")
//    public ResponseEntity<ApiResponse<Void>> resetLawyerPassword(@RequestParam String email, @RequestParam String newPassword) {
//
//        // validation
////            @NotBlank(message = "Password is required") @Size(min = 8, max = 100,
////                    message = "Password must be between 8 and 100 characters") @Pattern(
////                    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
////                    message = "Password must contain at least one digit, one lowercase, one  uppercase, and one special character")
//        return responseEntity(true ,
//                "Password has been reset successfully.",
//                HttpStatus.ACCEPTED,
//                lawyerService.changeLawyerPasswordByEmail(newPassword , email)
//                );
//    }
    @PutMapping("/{lawyerId}")
    public ResponseEntity<ApiResponse<AppUserResponse>> updateExistLawyerById(@RequestBody AppUserRequest appUserRequest , @PathVariable Long lawyerId ){
        return responseEntity(true,
                "Update lawyer id " + lawyerId + " successfully" ,
                HttpStatus.OK,
                adminService.modifiedExistLawyerById(appUserRequest , lawyerId));
    }
    @DeleteMapping("/{lawyerId}")
    public ResponseEntity<ApiResponse<Void>> removeExistLawyer(@PathVariable Long lawyerId ){
        return responseEntity(true,
                "Delete laywer id " + lawyerId+  " successfully",
                HttpStatus.OK,
                adminService.removeExistLawyerById(lawyerId));
    }
    // get user by gmail
//    @GetMapping("/{email}")
//    public ResponseEntity<ApiResponse<AppUserResponse>> getUserByEmail(@PathVariable  String email){
//        return responseEntity(true , "User name " + appUserService.getUserByEmail(email).getName() , HttpStatus.OK , appUserService.getUserByEmail(email));
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<AppUserResponse>> insertNewUser(@RequestBody UserRequest request){
//
//        System.out.println("My new user request from ui " + request);
//        return responseEntity(true , "Create new user successfully" , HttpStatus.CREATED , appUserService.insertNewUser(request));
//    }

//    @PutMapping("/reset-password")
//    @Operation(summary = "Reset Password")
//    public ResponseEntity<?> resetPassword(@RequestParam String email,
//
//                                           @NotBlank(message = "Password is required") @Size(min = 8, max = 100,
//                                                   message = "Password must be between 8 and 100 characters") @Pattern(
//                                                   regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
//                                                   message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character") @RequestParam String newPassword) {
//
//        String resultMessage = appUserService.resetPassword(email, newPassword);
//
//        ApiResponse<String> response = ApiResponse.<String>builder().success(true)
//                .message(resultMessage).status(HttpStatus.OK).code(HttpStatus.CREATED.value())
//                .timestamp(LocalDateTime.now()).build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}
