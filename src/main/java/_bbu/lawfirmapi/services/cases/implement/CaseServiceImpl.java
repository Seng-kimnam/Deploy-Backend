package _bbu.lawfirmapi.services.cases.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.exceptions.ResponseStatusException;
import _bbu.lawfirmapi.models.DTO.cases.request.CaseRequest;
import _bbu.lawfirmapi.models.DTO.cases.response.CaseResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Case;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.Entity.Court;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.repositories.CaseRepository;
import _bbu.lawfirmapi.repositories.ClientRepository;
import _bbu.lawfirmapi.repositories.CourtRepository;
import _bbu.lawfirmapi.services.cases.CaseService;
import _bbu.lawfirmapi.utils.MethodHelper;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService  {

    private final CaseRepository caseRepository;
    private final ClientRepository clientRepository;
    private final CourtRepository courtRepository;
    private final AppUserRepository appUserRepository;
    private final MethodHelper checkOutOfPage;

    @Override
    public Page<Case> getCaseList(Pageable pageable , Integer requestPage) {

        Page<Case> casePage = caseRepository.findAll(pageable) ;

        checkOutOfPage.isInvalidPage(casePage.getTotalPages() , requestPage);

        return casePage;

    }
    @Override
    @SneakyThrows
    public List<Case> getCaseNoPagination(){
        if(caseRepository.findAll().isEmpty()){
            throw new NotFoundException("List case not found.");
        }
        return caseRepository.findAll();
    }
    @Override
    public Case getCaseById(Long caseId){
        return caseRepository.findById(caseId).orElseThrow(
                () -> new NotFoundException("Case with Id " + caseId + " not found.")
        );
    }
    @SneakyThrows
    @Override
    public CaseResponse createNewCase(CaseRequest request) {
        boolean isExisting = caseRepository.existsByClient_ClientIdAndCourt_CourtIdAndTitleAndStartDate(
                request.getClientId(),
                request.getCourtId(),
                request.getTitle(),
                request.getStartedDate()
        );
        if(isExisting){
            throw new ResponseStatusException(
                    "This case is already exist in the list."
            );
        }
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new RuntimeException("Court not found"));

        Case newCase = request.toEntity();
        newCase.setClient(client);
        newCase.setCourt(court);
        newCase.setTitle(request.getTitle());
        newCase.setDescription(request.getDescription());
        newCase.setStatus(request.getStatus());
        newCase.setStartDate(request.getStartedDate());
        newCase.setEndDate(request.getEndedDate());
        newCase.setCreatedAt(LocalDateTime.now());
        return caseRepository.save(newCase).toResponse();
    }

    @Override
    public CaseResponse modifiedCaseById(Long caseId , CaseRequest caseRequest){
        Case currentCase = caseRepository.findById(caseId).orElseThrow(
                () -> new NotFoundException("Case with id " + caseId + " not found.")
        );
        Client client = clientRepository.findById(caseRequest.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Court court = courtRepository.findById(caseRequest.getCourtId())
                .orElseThrow(() -> new RuntimeException("Court not found"));


        currentCase.setTitle(caseRequest.getTitle());
        currentCase.setDescription(caseRequest.getDescription());
        currentCase.setStatus(caseRequest.getStatus());
        currentCase.setStartDate(caseRequest.getStartedDate());
        currentCase.setEndDate(caseRequest.getEndedDate());
        currentCase.setUpdatedAt(LocalDateTime.now());

        CaseResponse saveUpdatedCase = caseRepository.save(currentCase).toResponse();
        return saveUpdatedCase;
    }
    @Override
    public Void removeCaseById(Long caseId){
        if(caseRepository.existsById(caseId)){
            caseRepository.deleteById(caseId);
        }
        return null;
    }


    ///  filter

    @Override
    public Page<Case> fetchCaseByYearAndMonthAndDay(int year, int month, int day, Pageable pageable) {

        return caseRepository.findByYearAndMonthAndDay(year , month,day , pageable);
    }

    @Override
    public Page<Case> fetchCaseByYearAndMonth(int year, int month, Pageable pageable) {
        return caseRepository.findByYearAndMonth(year , month , pageable);

    }

    @Override
    public Page<Case> fetchCaseByYear(int year, Pageable pageable) {
        return caseRepository.findByYear(year , pageable);
    }
}
