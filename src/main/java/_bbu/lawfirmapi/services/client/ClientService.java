package _bbu.lawfirmapi.services.client;

import _bbu.lawfirmapi.models.DTO.client.request.ClientRequest;
import _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse;
import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.Entity.Client;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {


    Page<Client> getAllDetailClientsByEmail(Pageable pageable , Integer requestedPage , String email );
    List<Client> getAllClientList();
    Page<ClientListResponse> searchClientRequestByEmail(Pageable pageable , Integer requestPage,  String email );
    Page<ClientListResponse> getUniqueClient(Pageable pageable , Integer requestPage);
    Client getClientById(Long clientId);
    ClientResponse createNewClient(ClientRequest clientRequest) throws MessagingException;
    ClientResponse modifiedClientById(ClientRequest clientRequest , Long clientId) throws MessagingException;
    Void removeClientById(Long clientId);
}
