package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.client.request.ClientRequest;
import _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse;
import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.services.client.ClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clients")
//@SecurityRequirement(name = "bearerAuth")

public class ClientController extends BaseResponse {

    private final ClientService clientService;


    @GetMapping
    public ResponseEntity<ApiResponse<Page<ClientListResponse>>> getAllClientListResponse(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.unsorted());
        Page<ClientListResponse> clients = clientService.getUniqueClient(pageable , page );
        return responseEntity(
                true,
                "Get all unique client list",
                HttpStatus.OK,
                clients
        );
    }

    @GetMapping("/without-pagination")
    public ResponseEntity<ApiResponse<List<Client>>> fetchAllClients(){
        return responseEntity(true ,
                "Get client list successfully.",
                HttpStatus.OK,
                clientService.getAllClientList());
    }
//    @GetMapping("/search-client-req")
//    public ResponseEntity<ApiResponse<Page<ClientListResponse>>> searchClientRequestList(
////            @RequestParam String keyword,
//            @RequestParam(required = false) String email,
//            @PageableDefault(
//                    sort = "email",
//                    direction = Sort.Direction.ASC
//            )
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "5") Integer size,
//            @RequestParam(defaultValue = "clientId") String sortBy,
//            @RequestParam(defaultValue = "true") Boolean ascending
//    ){
//        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page - 1, size, sort);
//        Page<ClientListResponse> clients = clientService.searchClientRequestByEmail(pageable , page ,email );
//        return responseEntity(true ,
//                "Search client by email's keyword " + email +" successfully.",
//                HttpStatus.OK,
//                clients);
//    }
    @GetMapping("/search-client-req")
    public ResponseEntity<ApiResponse<Page<ClientListResponse>>> searchClientRequestList(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("email").ascending()
        );

        Page<ClientListResponse> clients =
                clientService.searchClientRequestByEmail(pageable,page , email);

        return responseEntity(
                true,
                "Search client by email's keyword " + email + " successfully.",
                HttpStatus.OK,
                clients
        );
    }
    @GetMapping("/request")
    public ResponseEntity<ApiResponse<Page<Client>>> getClientByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "clientId") String sortBy,
            @RequestParam(defaultValue = "false") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Client> clients = clientService.getAllDetailClientsByEmail(pageable , page ,email );
        return responseEntity(true ,
                "Get all client list",
                HttpStatus.OK,
                clients);
    }
    @GetMapping("/{clientId}")
    public ResponseEntity<ApiResponse<Client>> retrieveClientById(@PathVariable Long clientId){
        return responseEntity(true ,
                "get client id " +  clientId +  " with name " +  clientService.getClientById(clientId).getClientName() + " successfully",
                HttpStatus.ACCEPTED,
                clientService.getClientById(clientId));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> createNewClient(@RequestBody ClientRequest clientRequest) throws MessagingException {
        return responseEntity(true ,
                "Create new client successfully",
                HttpStatus.CREATED,
                clientService.createNewClient(clientRequest));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ApiResponse<ClientResponse>> modifiedClientById (
            @RequestBody ClientRequest clientRequest ,
            @PathVariable @Valid @Positive Long clientId) throws  MessagingException{
        return responseEntity(true,
                "Update client id " + clientId + " successfully",
                HttpStatus.ACCEPTED,
                clientService.modifiedClientById(clientRequest , clientId));
    }
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeClientById(Long clientId){
        return responseEntity(true ,
                "Delete client id " + clientId + " successfully",
                HttpStatus.ACCEPTED,
                clientService.removeClientById(clientId)
        );
    }
}
