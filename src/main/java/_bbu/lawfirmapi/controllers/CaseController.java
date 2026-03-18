package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.cases.request.CaseRequest;
import _bbu.lawfirmapi.models.DTO.cases.response.CaseResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Case;
import _bbu.lawfirmapi.services.cases.CaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
@RequestMapping("/api/v1/cases")
public class CaseController extends BaseResponse {

    private final CaseService caseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Case>>> getAllCase(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "caseId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Case> caseList =   caseService.getCaseList(pageable , page);
        return responseEntity(true,
                "Get all cases successfully",
                HttpStatus.OK,
                caseList);
    }

    @GetMapping("/no-pagination")
    public ResponseEntity<ApiResponse<List<Case>>> fetchCaseList(){
        return responseEntity(true ,
                "Get case list successfully",
                HttpStatus.ACCEPTED,
                caseService.getCaseNoPagination());
    }
    @GetMapping("/{caseId}")

    public ResponseEntity<ApiResponse<Case>> fetchCaseById(@PathVariable Long caseId){
        return responseEntity(true ,
                "Get case with id " + caseId + " successfully",
                HttpStatus.ACCEPTED,
                caseService.getCaseById(caseId));
    }

    @GetMapping("/filter-by-year")
    public ResponseEntity<ApiResponse<Page<Case>>> filterByYear(
            @RequestParam Integer year,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "caseId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return responseEntity(true,
                "Get cases by year " + year + " successfully",
                HttpStatus.OK,
                caseService.fetchCaseByYear(year, pageable));
    }

    @GetMapping("/filter-by-month")
    public ResponseEntity<ApiResponse<Page<Case>>> filterByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "caseId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return responseEntity(true,
                "Get cases by year " + year + " and month " + month + " successfully",
                HttpStatus.OK,
                caseService.fetchCaseByYearAndMonth(year, month, pageable));
    }

    @GetMapping("/filter-by-day")
    public ResponseEntity<ApiResponse<Page<Case>>> filterByDay(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "caseId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return responseEntity(true,
                "Get cases by date " + year + "-" + month + "-" + day + " successfully",
                HttpStatus.OK,
                caseService.fetchCaseByYearAndMonthAndDay(year, month, day, pageable));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CaseResponse>> createNewCase(@RequestBody CaseRequest caseRequest){

        return responseEntity(true ,
                "Create new case successfully",
                HttpStatus.CREATED,
                caseService.createNewCase(caseRequest));
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<ApiResponse<CaseResponse>> updateCaseByID(@PathVariable("caseId") @Valid @Positive Long caseId , @RequestBody CaseRequest caseRequest){
        return responseEntity(true ,
                "Update case with id " + caseId + " successfully",
                HttpStatus.ACCEPTED,
                caseService.modifiedCaseById(caseId , caseRequest));
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCaseById(@PathVariable("caseId") Long caseId){

        return responseEntity(true ,
                "Delete case with id " + caseId +" successfully",
                HttpStatus.ACCEPTED,
                caseService.removeCaseById(caseId));
    }





}
