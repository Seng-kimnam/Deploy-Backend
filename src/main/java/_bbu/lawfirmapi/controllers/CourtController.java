package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.court.request.CourtRequest;
import _bbu.lawfirmapi.models.DTO.court.response.CourtResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Court;
import _bbu.lawfirmapi.services.court.CourtService;
import _bbu.lawfirmapi.utils.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courts")
public class CourtController extends BaseResponse {

    public final CourtService courtService;

    @GetMapping("/{courtId}")
    public ResponseEntity<ApiResponse<Court>> getCourtById(@PathVariable Long courtId){
        return responseEntity(true ,
                "Get court with id " + courtId + " successfully" ,
                HttpStatus.OK ,
                courtService.getCourtById(courtId));
    }
    @GetMapping("/without-pagination")
    public ResponseEntity<ApiResponse<List<Court>>> getAllCourts(){
        return responseEntity(true ,
                "Get all court successfully" ,
                HttpStatus.OK ,
                courtService.getCourtListWithNoPagination());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Court>>> getAllCourtWithPagination(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "courtId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending()  : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1 , size , sort);
        Page<Court> courts = courtService.fetchAllCourtWithPagination(pageable , page);
        return responseEntity(true ,
                "Get all court successfully" ,
                HttpStatus.OK ,
                courts);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<CourtResponse>> createCourt(@RequestBody CourtRequest courtRequest){
        return responseEntity(true ,
                "Create new court successfully" ,
                HttpStatus.CREATED ,
                courtService.createNewCourt(courtRequest));
    }
    @PutMapping("/{courtId}")
    public ResponseEntity<ApiResponse<CourtResponse>> updateNewCourt(@RequestBody CourtRequest courtRequest , @PathVariable Long courtId){
        return responseEntity(true ,
                "Update court with name " + courtService.getCourtById(courtId).getCourtName() +  " to " + courtRequest.getCourtName(),
                HttpStatus.ACCEPTED,
                courtService.modifiedCourtById(courtRequest , courtId));
    }
    @DeleteMapping("/{courtId}")
    public ResponseEntity<ApiResponse<Void>> deleteExistCourtById(@PathVariable  Long courtId){
        return responseEntity(true ,
                "Delete court with id " + courtId + " successfully",
                HttpStatus.OK,
                courtService.removeCourtById(courtId));
    }




}
