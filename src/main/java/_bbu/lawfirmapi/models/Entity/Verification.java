package _bbu.lawfirmapi.models.Entity;

import java.time.LocalDateTime;

import _bbu.lawfirmapi.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "verification")
public class Verification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;
    @Column(name = "expire_date_time")
    private LocalDateTime expireDateTime;
    @Column(name = "verified_code")
    private String verifiedCode;
    @Column(name = "is_verified")
    private Boolean isVerified;
    @Column(name = "email")
    private String email;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "appuser_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_app_user_id")
    )
    private AppUser appUser;


}

