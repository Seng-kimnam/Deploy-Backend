package _bbu.lawfirmapi.services.expertise;

import _bbu.lawfirmapi.models.DTO.expertise.request.ExpertiseRequest;
import _bbu.lawfirmapi.models.DTO.expertise.response.ExpertiseResponse;
import _bbu.lawfirmapi.models.Entity.Expertise;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExpertiseService {

    Page<Expertise> fetchAllExpertise(Pageable pageable, Integer requestPage);
    List<Expertise> fetchAllExpertiseWithNoPagination();
    Expertise fetchExpertiseById(Integer expertiseId);

    ExpertiseResponse createNewExpertise(ExpertiseRequest expertiseRequest);

    ExpertiseResponse updateExistExpertiseById(ExpertiseRequest expertiseRequest , Integer expertiseId);

    Void removeExistExpertiseById(Integer expertiseId);

}
