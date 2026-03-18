package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.service.request.ServiceRequest;
import _bbu.lawfirmapi.models.DTO.service.response.ServiceResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Service;
import _bbu.lawfirmapi.services.law_service.ServiceLawyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController extends BaseResponse {

    private final ServiceLawyerService serviceLawyerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getAllService(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "serviceId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ServiceResponse> services = serviceLawyerService.getAllLawyerService(pageable , page);
        return responseEntity(true ,
                "Get service list successfully",
                HttpStatus.OK,
                services);
    }
    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long serviceId){
        return responseEntity(
                true ,
                "Get service with id " +  serviceId + " successfully " ,
                HttpStatus.ACCEPTED,
                serviceLawyerService.getLawyerServiceById(serviceId)
        );
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createNewLawyerService(@RequestBody ServiceRequest serviceRequest){
        return responseEntity(
                true ,
                "Create new service name " + serviceRequest.getServiceName()  + " successfully",
                HttpStatus.CREATED,
                serviceLawyerService.createNewLawyerService(serviceRequest)
        );
    }
    @PutMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateExistServiceById(@PathVariable Long serviceId, @RequestBody ServiceRequest serviceRequest){
        return responseEntity(
                true ,
                "Update service name " + serviceLawyerService.getLawyerServiceById(serviceId).getServiceName() + " to " + serviceRequest.getServiceName() +  " successfully",
                HttpStatus.ACCEPTED,
                serviceLawyerService.modifiedExistingLawyerServiceById(serviceId , serviceRequest)
        );
    }
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<Void>> deleteServiceByID(@PathVariable  Long serviceId ){
        return responseEntity(
                true ,
                "Delete service name " + serviceLawyerService.getLawyerServiceById(serviceId).getServiceName() +   " successfully.",
                HttpStatus.ACCEPTED,
                serviceLawyerService.removeLawyerServiceById(serviceId)
        );
    }

}
