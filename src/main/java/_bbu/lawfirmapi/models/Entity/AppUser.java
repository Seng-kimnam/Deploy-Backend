package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.Enumerations.Gender;
import _bbu.lawfirmapi.models.Enumerations.LawyerStatus;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true, exclude = {"expertises"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "app_users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"appUserId" , "fullName", "gender","lawyerStatus", "email" , "phoneNumber","password" , "image" , "facebookLink" , "tiktokLink" , "telegramLink" ,"description" , "title","name" , "username" , "role", "expertises", "createdAt" , "updatedAt" })

@ToString
//@JsonPropertyOrder({"appUserId" , "" })
public class AppUser extends BaseEntity implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appuser_id")
    private Long appUserId;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "role_id" , referencedColumnName = "role_id")
    private Role role;
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    // create for many-many between appuser & expertise
    @JoinTable(
            name = "appuser_expertise",
            joinColumns = @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "expertise_id")
    )
//    @ToString.Exclude
//    @JsonIgnore
    private Set<Expertise> expertises;
    @Column(name = "full_name")
    private String fullName;
    @OneToMany(mappedBy = "appUser" ,cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Document> documents;
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "lawyer_status")
    private LawyerStatus lawyerStatus;
    @Column(name ="email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "image")
    private String image;
    @Column(name = "facebook_link")
    private String facebookLink;
    @Column(name = "tiktok_link")
    private String tiktokLink;
    @Column(name = "telegram_link")
    private String telegramLink;
    @Column(name = "failed_attempt_count")
    private  Integer failedAttemptCount = 0;
    @Column(name = "lockout_time")
    private java.time.LocalDateTime lockoutTime;
    @Column(name = "account_locked")
    private  Boolean accountLocked = false;

    @Column (name = "description" , columnDefinition = "TEXT")
    private String description;
    @Column(name = "title")
    private String title;


    @OneToMany(mappedBy = "appUser" , cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Verification> verifications;

    public AppUser(Object o,
                   String fullName,
                   Gender gender,
                   LawyerStatus lawyerStatus,
                   String email,
                   String phoneNumber,
                   String password,
                   Integer roleId,
                   Set<Integer> expertiseIdList,
                   String image,
                   String description,
                   String title,
                   String facebookLink,
                   String tiktokLink,
                   String telegramLink
    ) {

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    public AppUserResponse toResponse(){
        return new AppUserResponse(
                this.appUserId,
                this.fullName,
                this.gender,
                this.lawyerStatus,
                this.email,
                this.phoneNumber,
                this.password,
                this.role.getRoleName(),
                this.expertises.stream().map(
                        Expertise::getExpertName
                ).collect(Collectors.toSet()),
                this.image,
                this.description,
                this.title,
                this.facebookLink,
                this.tiktokLink,
                this.telegramLink,
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }


    @Override
    public String getUsername() {
        return email;
    }

    public String getName(){
        return this.fullName;
    }


}


