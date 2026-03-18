package _bbu.lawfirmapi.services.admin.implement;

import _bbu.lawfirmapi.exceptions.EmailAlreadyExistException;
import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.MonthlyStatistic;
import _bbu.lawfirmapi.models.DTO.appuser.request.AppUserRequest;
import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ChartResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Expertise;
import _bbu.lawfirmapi.models.Entity.Role;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.repositories.ClientRepository;
import _bbu.lawfirmapi.repositories.ExpertiseRepository;
import _bbu.lawfirmapi.repositories.RoleRepository;
import _bbu.lawfirmapi.services.admin.AdminService;
import _bbu.lawfirmapi.utils.MethodHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    // injection
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ExpertiseRepository expertiseRepository;
    private final ClientRepository clientRepo;
    private final MethodHelper checkOutOfPage;


    private final String adminEmail = "gclawgroup168@gmail.com";


    public AppUser getCurrentAdminEntity() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        System.out.println("DD" + auth);
            if (auth == null || !auth.isAuthenticated()
                    || auth.getPrincipal().equals("anonymousUser")) {
                throw new RuntimeException("Unauthenticated");
            }

            String email = auth.getName();
            AppUser currentProfile = appUserRepository.findByEmailWithRole(email)
                    .orElseThrow(() -> new NotFoundException("Admin with email " + email +  " not found."));
//            System.out.println("Current User: " + currentProfile.getEmail() + " | Role: " + currentProfile.getRole());

            return currentProfile;
    }


    @Override
    public AppUserResponse getCurrentAdminProfile()  {

        return getCurrentAdminEntity().toResponse();
    }

    @Override
    public AppUserResponse updateProfileAdmin(AppUserRequest appUserRequest) {

        AppUser admin = getCurrentAdminEntity();

        Set<Expertise> expertiseEntities = appUserRequest.getExpertiseIdList().stream()
                .map(id -> expertiseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(
                                "Expertise with id " + id + " not found")))
                .collect(Collectors.toSet());

        Role role = roleRepository.findById(appUserRequest.getRoleId())
                .orElseThrow(() -> new NotFoundException(
                        "Role id " + appUserRequest.getRoleId() + " not found"));

        String restOfPhoneNumber = appUserRequest.getPhoneNumber().trim().strip();
        String phoneNumberNoWhiteSpace = restOfPhoneNumber.replaceAll("\\s+","");
        admin.setFullName(appUserRequest.getFullName());
        admin.setGender(appUserRequest.getGender());
        admin.setLawyerStatus(appUserRequest.getLawyerStatus());
        admin.setPhoneNumber(phoneNumberNoWhiteSpace);
        admin.setTitle(appUserRequest.getTitle());
        admin.setDescription(appUserRequest.getDescription());
        admin.setFacebookLink(appUserRequest.getFacebookLink());
        admin.setTiktokLink(appUserRequest.getTiktokLink());
        admin.setTelegramLink(appUserRequest.getTelegramLink());
        admin.setImage(appUserRequest.getImage());
        admin.setRole(role);
        admin.setExpertises(expertiseEntities);

        if (appUserRequest.getPassword() != null &&
                !appUserRequest.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        }
        AppUserResponse updated = appUserRepository.save(admin).toResponse();
        return updated;
    }

    @Override
    public Page<AppUserResponse> getAllUser(Pageable pageable , Integer requestPage){
        Page<AppUser> lawyerList = appUserRepository.findAllWithExpertisesAndPagination(pageable);
        if (lawyerList.isEmpty()){
            throw new NotFoundException("No lawyer list here.");
        }
        checkOutOfPage.isInvalidPage(lawyerList.getTotalPages() , requestPage);


        return lawyerList.map(AppUser::toResponse);
    }
    @Override
    public AppUserResponse getLawyerById(Long lawyerId){

        AppUser lawyer = appUserRepository.findLawyerByAppUserId(lawyerId)
                .orElseThrow(
                        () -> new NotFoundException("Lawyer with id " + lawyerId + " not found" )
                );

        return lawyer.toResponse();
    }
    @Override
    public List<AppUserResponse> getAllLawyerListNoPagination(){
        List<AppUser> lawyerList = appUserRepository.findAllWithExpertisesNoPagination();
        if (lawyerList.isEmpty()){
            throw new NotFoundException("No lawyer list here.");
        }

        return lawyerList.stream()
                .map(AppUser::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AppUser userDetail = appUserRepository.findByEmailWithRole(email.trim()).orElseThrow(
                () -> new NotFoundException("Credential with email name" + email +  " not found.")
        );

        if (userDetail == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        return userDetail;
    }

    @Override
    public void checkIsEmailExist(String email){
        if (appUserRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistException("Email already exists: " + email);
        }
    }
    @Override
    public AppUserResponse registerNewLawyer(AppUserRequest appUserRequest) {

        AppUser newLawyer = appUserRequest.toEntity();
        //  Check for existing email
        checkIsEmailExist(appUserRequest.getEmail());

        // Convert expertise IDs to entities
        Set<Expertise> expertiseEntities = appUserRequest.getExpertiseIdList().stream()
                .map(id -> expertiseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Expertise with id " + id + " not found")))
                .collect(Collectors.toSet());


        // Fetch role
        Role role = roleRepository.findById(appUserRequest.getRoleId())
                .orElseThrow(() -> new NotFoundException("Invalid role ID: " + appUserRequest.getRoleId()));

        newLawyer.setFullName(appUserRequest.getFullName());
        newLawyer.setGender(appUserRequest.getGender());
        newLawyer.setLawyerStatus(appUserRequest.getLawyerStatus());
        newLawyer.setEmail(appUserRequest.getEmail().trim());
        newLawyer.setPhoneNumber(appUserRequest.getPhoneNumber());
        newLawyer.setPassword(passwordEncoder.encode(appUserRequest.getPassword().trim()));
        newLawyer.setRole(role);
        newLawyer.setExpertises(expertiseEntities);
        newLawyer.setImage(appUserRequest.getImage());
        newLawyer.setDescription(appUserRequest.getDescription());
        newLawyer.setTitle(appUserRequest.getTitle());
        newLawyer.setFacebookLink(appUserRequest.getFacebookLink());
        newLawyer.setTiktokLink(appUserRequest.getTiktokLink());
        newLawyer.setTelegramLink(appUserRequest.getTelegramLink());

        AppUserResponse savedNewLawyer = appUserRepository.save(newLawyer).toResponse();
        return savedNewLawyer;
    }


    @Override
    public AppUserResponse modifiedExistLawyerById(AppUserRequest appUserRequest , Long lawyerId){

        AppUser currentLawyer = appUserRepository.findById(lawyerId).orElseThrow(
                () -> new NotFoundException("Lawyer Id " + lawyerId + " not found.")
        );

        // Attach existing Role
        Role role = roleRepository.findById(appUserRequest.getRoleId())
                .orElseThrow(() -> new NotFoundException("Invalid role ID: " + appUserRequest.getRoleId()));


        // Convert expertise IDs to entities
        Set<Expertise> expertiseEntities = appUserRequest.getExpertiseIdList().stream()
                .map(id -> expertiseRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Expertise with id " + id + " not found")))
                .collect(Collectors.toSet());


        currentLawyer.setFullName(appUserRequest.getFullName());
        currentLawyer.setEmail(appUserRequest.getEmail().trim());
        currentLawyer.setGender(appUserRequest.getGender());
        currentLawyer.setLawyerStatus(appUserRequest.getLawyerStatus());
        currentLawyer.setPhoneNumber(appUserRequest.getPhoneNumber());
        currentLawyer.setPassword(appUserRequest.getPassword().trim());
        currentLawyer.setImage(appUserRequest.getImage());
        currentLawyer.setDescription(appUserRequest.getDescription());
        currentLawyer.setTitle(appUserRequest.getTitle());
        currentLawyer.setFacebookLink(appUserRequest.getFacebookLink());
        currentLawyer.setTiktokLink(appUserRequest.getTiktokLink());
        currentLawyer.setTelegramLink(appUserRequest.getTelegramLink());
        currentLawyer.setRole(role);
        currentLawyer.setExpertises(expertiseEntities);

        AppUserResponse updatedLawyer = appUserRepository.save(currentLawyer).toResponse();

            return updatedLawyer;

    }

    @Override
    public Void removeExistLawyerById(Long appUserId){
        if(!appUserRepository.existsById(appUserId)){
          throw new NotFoundException("Lawyer id " + appUserId + " not found.");
        }
            appUserRepository.deleteById(appUserId);
        return null;
    }

    @Override
    public List<Integer> fetchMonthlyStats(int year){
        List<Object[]> result = clientRepo.getMonthlyStatistic(year);
        int[] data = new int[12];

        for (Object[] row : result) {
            int month = ((Number) row[0]).intValue() - 1;
            int count = ((Number) row[1]).intValue();
            data[month] = count;
        }
        return Arrays.stream(data).boxed().toList();
    }
    @Override
    public List<Integer> fetchOnlyMonthStats() {

        List<MonthlyStatistic> result = clientRepo.getOnlyMonthlyStatistic();
        int[] data = new int[12];


        for (MonthlyStatistic row : result) {

            // handle is null
            if (row.getMonth() == null || row.getTotal() == null) {
                continue;
            }

            // 1–12 = 0–11
            int monthIndex = row.getMonth() - 1;

            // Prevent invalid index
            if (monthIndex < 0 || monthIndex > 11) {
                continue;
            }

            data[monthIndex] = row.getTotal().intValue();
        }

        return Arrays.stream(data).boxed().toList();
    }

    @Override
    public List<Integer> fetchQuarterlyStats(int year){
        List<Object[]> result = clientRepo.getQuarterlyStatistic(year);
        int[] data = new int[4];

        for (Object[] row : result) {
            int month = ((Number) row[0]).intValue() - 1;
            int count = ((Number) row[1]).intValue();
            data[month] = count;
        }
        return Arrays.stream(data).boxed().toList();
    }
    @Override
    public ChartResponse fetchAnnualStats(){
        List<Object[]> result = clientRepo.getAnnualStatistic();
        List<String> categories  = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        for (Object[] row : result) {
            if (row[0] == null) continue;
            String year = row[0].toString();
            int count = ((Number) row[1]).intValue();

            categories.add(year);
            data.add(count);
        }

        return  new ChartResponse(
                "annually",
                null,
                categories,
                data
        );
    }
}
