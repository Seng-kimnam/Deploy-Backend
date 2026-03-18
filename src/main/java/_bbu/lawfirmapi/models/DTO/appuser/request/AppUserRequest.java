package _bbu.lawfirmapi.models.DTO.appuser.request;

import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Enumerations.Gender;
import _bbu.lawfirmapi.models.Enumerations.LawyerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "User registration request")
public class AppUserRequest {

    @NotBlank
    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @Schema(description = "Gender of the user", example = "MALE")
    private Gender gender;

    @Schema(description = "Current status of the lawyer", example = "ACTIVE")
    private LawyerStatus lawyerStatus;

    @Schema(description = "User email", example = "Ex: john.doe@gmail.com")
    private String email;

    @Schema(description = "Phone number", example = "Ex: +85512345678")
    private String phoneNumber;

    @Schema(description = "Password", example = "strongpassword123")
    private String password;

    @Schema(description = "User role ID", example = "1")
    private Integer roleId;

    @Schema(description = "List of expertise IDs", example = "[1, 2, 3]")
    private Set<Integer> expertiseIdList;

    @Schema(description = "Profile image URL", example = "https://example.com/image.jpg")
    private String image;

    @Schema(description = "Short description or bio", example = "Experienced lawyer specializing in corporate law.")
    private String description;
    private String title;

    private String facebookLink;
    private String tiktokLink;
    private String telegramLink;

    public AppUser toEntity(){
        return new AppUser(null ,
                this.fullName ,
                this.gender,
                this.lawyerStatus,
                this.email ,
                this.phoneNumber ,
                this.password ,
                this.roleId ,
                this.expertiseIdList ,
                this.image,
                this.description,
                this.title,
                this.facebookLink,
                this.tiktokLink,
                this.telegramLink
        );
    }
}
