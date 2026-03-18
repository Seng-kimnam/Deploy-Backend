package _bbu.lawfirmapi.services.auth;


import _bbu.lawfirmapi.models.DTO.appuser.request.AppUserRequest;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService  {

      AppUser getCurrentUser();
      String sendNews(String email);
      AppUserResponse getProfile();
      AppUserResponse verifyOTPByEmail(String email, String otp, Boolean isOTPRegister) throws MessagingException;
      Void resetNewPasswordByEmail(String email ,  String newPassword);
      Void resendOTP(String email) throws  MessagingException;


}
