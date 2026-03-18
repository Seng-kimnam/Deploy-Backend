package _bbu.lawfirmapi.services.court.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.court.request.CourtRequest;
import _bbu.lawfirmapi.models.DTO.court.response.CourtResponse;
import _bbu.lawfirmapi.models.Entity.Court;
import _bbu.lawfirmapi.repositories.CourtRepository;
import _bbu.lawfirmapi.services.court.CourtService;
import _bbu.lawfirmapi.utils.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final MethodHelper methodHelper;


    @Override
    public Court getCourtById(Long courtId){
        return  courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Court with id " + courtId + " not found update"));
    }
    @Override
    public List<Court> getCourtListWithNoPagination() {
//        System.out.println("sd" + courtRepository.findAll());
        return Optional.of(courtRepository.findAll())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new NotFoundException("No court list found."));
    }
    @Override
    public Page<Court> fetchAllCourtWithPagination(Pageable pageable , Integer requestedPage){

        Page<Court> courts = courtRepository.findAll(pageable);

        methodHelper.isInvalidPage(courts.getTotalPages() , requestedPage);

        if(courts.isEmpty()){
            throw new NotFoundException("No list court found");

        }
        return courts;
    }
    @Override
    public CourtResponse createNewCourt(CourtRequest courtRequest) {

        Court newCourt = courtRequest.toEntity();
        newCourt.setCourtName(courtRequest.getCourtName());
        newCourt.setCourtType(courtRequest.getCourtType());
        newCourt.setContactNumber(courtRequest.getContactNumber());
        newCourt.setLocation(courtRequest.getLocation());
        newCourt.setCreatedAt(LocalDateTime.now());
        return courtRepository.save(newCourt).toResponse();
    }


    @Override
    public CourtResponse modifiedCourtById(CourtRequest courtRequest, Long courtId) {
        Court currentCourt = courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Court with id " + courtId+ " not found update"));
        currentCourt.setCourtName(courtRequest.getCourtName());
        currentCourt.setCourtType(courtRequest.getCourtType());
        currentCourt.setLocation(courtRequest.getLocation());
        currentCourt.setContactNumber(courtRequest.getContactNumber());
        currentCourt.setUpdatedAt(LocalDateTime.now());
        Court newCourt = courtRepository.save(currentCourt);
        return newCourt.toResponse();
    }

    @Override
    public Void removeCourtById(Long courtId) {
        courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Court with id " + courtId+ " not found for delete"));
        return null;
    }
}
