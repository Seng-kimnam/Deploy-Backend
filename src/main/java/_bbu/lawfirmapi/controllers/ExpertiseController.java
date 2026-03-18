package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.expertise.request.ExpertiseRequest;
import _bbu.lawfirmapi.models.DTO.expertise.response.ExpertiseResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.Entity.Expertise;
import _bbu.lawfirmapi.services.expertise.ExpertiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expertises")
public class ExpertiseController extends BaseResponse {
    private final ExpertiseService expertiseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Expertise>>> getExpertiseList(
            @RequestParam(defaultValue = "1")  Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "expertiseId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Expertise> expertiseList = expertiseService.fetchAllExpertise(pageable , page );

        return responseEntity(true ,
                "Getting expertise list successfully",
                HttpStatus.OK,
        expertiseList);
    }
    @GetMapping("/without-pagination")
    public ResponseEntity<ApiResponse<List<Expertise>>> getExpertiseListNoPage(){
        return responseEntity(true ,
                "Get all expertise list without pagination successfully.",
                HttpStatus.OK,
                expertiseService.fetchAllExpertiseWithNoPagination());
    }
    @GetMapping("/{expertiseId}")
    public ResponseEntity<ApiResponse<Expertise>> fetchExpertiseById(@PathVariable Integer expertiseId){
        return responseEntity(
                true,
                "Get expertise with id " + expertiseId + " successfully",
                HttpStatus.ACCEPTED,
                expertiseService.fetchExpertiseById(expertiseId)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpertiseResponse>> createExpertise(@RequestBody ExpertiseRequest expertiseRequest){

        return responseEntity(true ,
                "Create new expertise name " + expertiseRequest.getExpertName() +  " successfully" ,
                HttpStatus.CREATED,
                expertiseService.createNewExpertise(expertiseRequest));
    }
    @PutMapping("/{expertiseId}")
    public ResponseEntity<ApiResponse<ExpertiseResponse>> updateExistExpertise(
            @RequestBody ExpertiseRequest expertiseRequest,
            @PathVariable Integer expertiseId ){
        return responseEntity(true,
                "update expertise name " +  expertiseService.fetchExpertiseById(expertiseId).getExpertName()  + " to " + expertiseRequest.getExpertName()  +  " successfully",
                HttpStatus.ACCEPTED ,
                expertiseService.updateExistExpertiseById(expertiseRequest , expertiseId));
    }
    @DeleteMapping("/{expertiseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExistingExpertise(@PathVariable Integer expertiseId){
        return responseEntity(true ,
                "Delete expertise id " + expertiseId + " name " + expertiseService.fetchExpertiseById(expertiseId).getExpertName() + " successfully",
                HttpStatus.ACCEPTED,
                expertiseService.removeExistExpertiseById(expertiseId));

    }
}
