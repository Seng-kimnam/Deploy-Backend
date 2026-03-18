package _bbu.lawfirmapi.models.DTO.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {
    private String token;
    private UserDetails currentUser;
    private String expiredTime;
}
