package _bbu.lawfirmapi.models.DTO.appuser.response;


import _bbu.lawfirmapi.models.Enumerations.Gender;
import _bbu.lawfirmapi.models.Enumerations.LawyerStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppUserResponse   {
    private Long appUserId;
    private String fullName;
    private Gender gender;
    private LawyerStatus lawyerStatus;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private Set<String> expertises;
    private String image;
    private String description;
    private String title;
    private String facebookLink;
    private String tiktokLink;
    private String telegramLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
