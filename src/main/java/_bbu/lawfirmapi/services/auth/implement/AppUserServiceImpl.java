package _bbu.lawfirmapi.services.auth.implement;

import _bbu.lawfirmapi.exceptions.EmailAlreadyExistException;
import _bbu.lawfirmapi.exceptions.ExpireOTPCodeException;
import _bbu.lawfirmapi.exceptions.InvalidException;
import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.appuser.request.AppUserRequest;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Role;
import _bbu.lawfirmapi.models.Entity.Verification;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.repositories.RoleRepository;
import _bbu.lawfirmapi.repositories.VerificationRepository;
import _bbu.lawfirmapi.services.auth.AppUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
//@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final VerificationRepository verificationRepo;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public AppUser getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    @Override
    public AppUserResponse getProfile() {
        AppUser appUser = appUserRepository.findById(getCurrentUser().getAppUserId())
                .orElseThrow(() -> new NotFoundException("The profile with id " + getCurrentUser().getAppUserId() + " not found"));

        return appUser.toResponse();
    }
    @Override
    @SneakyThrows
    public String sendNews(String email){

        // prepare mail to user
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // set up thymeleaf
        Context context = new Context();

        context.setVariable("subject" , "Test sending mail from Law firm.");
        context.setVariable("user" , email);
        context.setVariable("message" , "Hello from law firm");

        // Process the template

        String htmlContent = templateEngine.process("notification-template", context);
        mimeMessageHelper.setSubject("New Announcement from GCLAW group");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setFrom(adminEmail);
        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);

        return "This new announcement have been sending to " + email;
    }

    @Override
     public  AppUserResponse verifyOTPByEmail(String email, String otp, Boolean isOTPRegister) throws MessagingException {

        Verification userVerification = verificationRepo.findTopByEmailOrderByExpireDateTimeDesc(email.trim())
                .orElseGet(Verification::new);

        // validate user verify is null
        if (userVerification == null) {
            throw new ExpireOTPCodeException("The user verify is not found with this email.");
        }
        // validate expire OTP code
        if (LocalDateTime.now().isAfter(userVerification.getExpireDateTime())) {
            throw new ExpireOTPCodeException("The OTP code has expired.");
        }
        // validate OTP mismatch
        if (!userVerification.getVerifiedCode().equals(otp)) {
            throw new InvalidException("Invalid OTP.");
        }
        AppUser appUser = appUserRepository.findAppUserByEmail(email.trim())
                .orElseThrow(() -> new NotFoundException("App user not found."));
        // send to email of admin for approval
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        // Thymeleaf context setup
        Context context = new Context();
        context.setVariable("fullName", appUser.getFullName());
        context.setVariable("email", email);
        context.setVariable("phoneNumber", appUser.getPhoneNumber());

        // Process the template
        String htmlContent = templateEngine.process("need-approval-form", context);

        mimeMessageHelper.setSubject("Email Request Registration ");
        mimeMessageHelper.setTo(adminEmail);
        mimeMessageHelper.setFrom(adminEmail);
        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);

        return appUser.toResponse();
    }

    @Override
    public Void resetNewPasswordByEmail(String email ,  String newPassword){

        String newPasswordEncoder = passwordEncoder.encode(newPassword.trim());

        int updatedNewPassword = appUserRepository.resetPassword(newPasswordEncoder , email.trim());

        if(updatedNewPassword == 0){
            throw new RuntimeException("User not found");
        }

        return null;

    }

    @Override
    @Transactional
    public Void resendOTP(String email) throws MessagingException {

        AppUser emailUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));

        // generate OTP
        String otp = String.format("%06d", new Random().nextInt(1_000_000));

        // get latest verification (if exists)
        Verification verification = verificationRepo
                .findTopByEmailOrderByExpireDateTimeDesc(email)
                .orElseGet(Verification::new);

        // update or create
        verification.setExpireDateTime(LocalDateTime.now().plusMinutes(5));
        verification.setVerifiedCode(otp);
        verification.setIsVerified(true);
        verification.setEmail(email);
        verification.setAppUser(emailUser);

        verificationRepo.save(verification); // UPDATE or INSERT automatically

        // send email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("user", emailUser.getFullName());

        String htmlContent = templateEngine.process("otp-form", context);

        helper.setSubject("Email Verify Company name");
        helper.setTo(emailUser.getEmail());
        helper.setFrom(adminEmail);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
        return null;
    }






}
