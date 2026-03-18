package _bbu.lawfirmapi.services.expertise.implement;

import _bbu.lawfirmapi.exceptions.IllegalArgumentException;
import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.expertise.request.ExpertiseRequest;
import _bbu.lawfirmapi.models.DTO.expertise.response.ExpertiseResponse;
import _bbu.lawfirmapi.models.Entity.Expertise;
import _bbu.lawfirmapi.repositories.ExpertiseRepository;
import _bbu.lawfirmapi.services.expertise.ExpertiseService;
import _bbu.lawfirmapi.utils.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertiseServiceImpl implements ExpertiseService {

    private final ExpertiseRepository expertiseRepo;
    private final MethodHelper checkOutOfPage;

    @Override
    public List<Expertise> fetchAllExpertiseWithNoPagination(){
        if(expertiseRepo.findAll().isEmpty()){
            throw new NotFoundException("There is none expertise found");
        }
        return expertiseRepo.findAll();
    }
    @Override
    public Page<Expertise> fetchAllExpertise(Pageable pageable , Integer requestedPage) {

        Page<Expertise> page = expertiseRepo.findAll(pageable);
        if (page.getTotalElements() < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        checkOutOfPage.isInvalidPage(page.getTotalPages(), requestedPage);

        if (page.isEmpty()) {
            throw new NotFoundException("No expertise list found.");
        }

        return page;
    }

    @Override
    public Expertise fetchExpertiseById(Integer expertiseId) {

        return expertiseRepo.findById(expertiseId).orElseThrow(() -> new NotFoundException("Expertise with id " + expertiseId + " not found."));
    }

    @Override
    public ExpertiseResponse createNewExpertise(ExpertiseRequest expertiseRequest) {
        Expertise newExpertise  = expertiseRequest.toEntity();
        newExpertise.setExpertName(expertiseRequest.getExpertName());
        newExpertise.setCreatedAt(LocalDateTime.now());
        newExpertise.setUpdatedAt(LocalDateTime.now());
        return expertiseRepo.save(newExpertise).toResponse();
    }

    @Override
    public ExpertiseResponse updateExistExpertiseById(ExpertiseRequest expertiseRequest, Integer expertiseId) {
        Expertise previousExpertise = expertiseRepo.findById(expertiseId).orElseThrow(() -> new NotFoundException("Cannot update expertise name " +
                expertiseRequest.getExpertName() + "Because expertise id" +  expertiseId + " not found."));
        previousExpertise.setExpertName(expertiseRequest.getExpertName());
        previousExpertise.setUpdatedAt(LocalDateTime.now());
        Expertise updatedExpertise = expertiseRepo.save(previousExpertise);
        return updatedExpertise.toResponse();
    }

    @Override
    public Void removeExistExpertiseById(Integer expertiseId) {
        expertiseRepo.findById(expertiseId).orElseThrow(() -> new NotFoundException("Expertise with id " + expertiseId + " not found."));
        expertiseRepo.deleteById(expertiseId);
        return null;
    }
}
