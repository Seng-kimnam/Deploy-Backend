package _bbu.lawfirmapi.services.court;

import _bbu.lawfirmapi.models.DTO.court.request.CourtRequest;
import _bbu.lawfirmapi.models.DTO.court.response.CourtResponse;
import _bbu.lawfirmapi.models.Entity.Court;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourtService {
    Court getCourtById(Long courtId);
    List<Court> getCourtListWithNoPagination ();
    Page<Court> fetchAllCourtWithPagination(Pageable pageable , Integer requestedPage);
    CourtResponse createNewCourt( CourtRequest courtRequest);
    CourtResponse modifiedCourtById(CourtRequest courtRequest , Long courtId);
    Void removeCourtById(Long courtId);
}
