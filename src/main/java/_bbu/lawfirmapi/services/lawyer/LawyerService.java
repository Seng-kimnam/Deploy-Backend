package _bbu.lawfirmapi.services.lawyer;

import _bbu.lawfirmapi.models.DTO.appuser.response.AppUserResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Task;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LawyerService {

    Page<Task> getTaskByLawyerEmail( Pageable pageable, Integer requestPage ,  String email );
    List<AppUserResponse> fetchAllLawyers ();
    AppUserResponse getCurrentLawyerProfile();
    AppUser fetchLawyerById(Long lawyerId);

    Void changeLawyerPasswordByEmail( String newPassword , String email);

    List<AppUserResponse> findLawyerByUsernameORPhoneNumberOREmail(String keyword);



}
