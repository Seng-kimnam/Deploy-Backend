package _bbu.lawfirmapi.services.admin;

import _bbu.lawfirmapi.models.DTO.appuser.request.AppUserRequest;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ChartResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface AdminService extends UserDetailsService {
    //    void validateUserByEmail(String email);

    AppUserResponse getCurrentAdminProfile();
    AppUser getCurrentAdminEntity();

    UserDetails loadUserByUsername(String email);

    Page<AppUserResponse> getAllUser (Pageable pageable , Integer requestPage);

    AppUserResponse getLawyerById(Long lawyerId);

    List<AppUserResponse> getAllLawyerListNoPagination();

    List<Integer> fetchMonthlyStats(int year);
    List<Integer> fetchOnlyMonthStats();

    List<Integer> fetchQuarterlyStats(int year);
    ChartResponse fetchAnnualStats();



//    AppUserResponse findByEmail(String email);

//    AppUserResponse findByEmail(String userEmail);

    AppUserResponse registerNewLawyer(AppUserRequest appUserRequest);

    AppUserResponse modifiedExistLawyerById(AppUserRequest appUserRequest , Long lawyerId);

    AppUserResponse updateProfileAdmin(AppUserRequest appUserRequest);

    Void removeExistLawyerById(Long appUserId);
    void checkIsEmailExist(String email);
//    AppUserResponse getProfile();

//    void removeProfile();

//    String reSendOTP(String email);

//    AppUserResponse verifyOTP(String email, String otp, Boolean isOTPRegister);
//
//    String resetPassword(String email, String newPassword);

//    AppUserResponse updateProfile(@Valid AppUserUpdateRequest appUserUpdateRequest);
//
//    void updateUserPassword(@Valid PasswordUpdateRequest passwordUpdateRequest);
}
