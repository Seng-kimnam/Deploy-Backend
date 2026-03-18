package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.services.auth.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verifications")
public class VerificationController extends BaseResponse {
    public final AppUserService appUserService;

    @PostMapping("/resend")
    @Operation(summary = "Resend Verified OTP")
    public ResponseEntity<?> resentOTP(@RequestParam String email) throws MessagingException {

        return responseEntity(true ,
                "OTP resent successfully.",
                HttpStatus.OK,
                appUserService.resendOTP(email)
                );
    }
    @PutMapping("/verify-reset-password")
    @Operation(summary = "Verify resetPassword email with OTP")
    public ResponseEntity<?> verifyOTPResetPassword(@RequestParam String email,
                                                    @RequestParam String otp) throws MessagingException {
        AppUserResponse appUserResponse = appUserService.verifyOTPByEmail(email, otp, false);

        return responseEntity(true ,
                "Verify OTP Reset Password successfully",
                HttpStatus.OK,
                appUserResponse);
    }
}
