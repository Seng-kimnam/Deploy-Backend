package _bbu.lawfirmapi.services.law_service;

import _bbu.lawfirmapi.models.DTO.service.request.ServiceRequest;
import _bbu.lawfirmapi.models.DTO.service.response.ServiceResponse;
import _bbu.lawfirmapi.models.Entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceLawyerService {

    ServiceResponse getLawyerServiceById(Long serviceId);
//    Page<ServiceResponse> fetchServiceByKeyword(String keyword);
    Page<ServiceResponse> getAllLawyerService(Pageable pageable , Integer requestedPage);
    ServiceResponse createNewLawyerService(ServiceRequest serviceRequest);
    ServiceResponse modifiedExistingLawyerServiceById(Long serviceId, ServiceRequest serviceRequest);
    Void removeLawyerServiceById(Long serviceId);

}
