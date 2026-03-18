package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.services.auth.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/api/v1/app-user")
@RequiredArgsConstructor
// this controller is use for lawyer which is app user for track email to notification  for client
public class AppUserController {
    private final AppUserService appUserService;
    @PostMapping("/send")
    @Operation(summary = "Send new event")
    public ResponseEntity<?> resentOTP(@RequestParam String email) {

        String resent = appUserService.sendNews(email);

        ApiResponse<AppUserResponse> response = ApiResponse.<AppUserResponse>builder().success(true)
                .message(resent).status(HttpStatus.OK).code(HttpStatus.OK.value())
                .timestamps(LocalDateTime.now()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
