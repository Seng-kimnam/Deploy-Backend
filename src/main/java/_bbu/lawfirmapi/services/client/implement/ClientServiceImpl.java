package _bbu.lawfirmapi.services.client.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.client.request.ClientRequest;
import _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse;
import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.Enumerations.ClientStatus;
import _bbu.lawfirmapi.repositories.ClientRepository;
import _bbu.lawfirmapi.services.client.ClientService;
import _bbu.lawfirmapi.utils.MethodHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final MethodHelper checkOutOfPage;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        return (AppUser) authentication.getPrincipal();
    }

    public Page<Client> getAllDetailClientsByEmail(Pageable pageable, Integer requestedPage, String email) {
        Page<Client> clients = clientRepository.findByEmail(pageable, email);

        checkOutOfPage.isInvalidPage(clients.getTotalPages(), requestedPage);

        if (clients.isEmpty()) {
            throw new NotFoundException("No client list found");
        }

        return clients;
    }

    @Override
    public List<Client> getAllClientList() {
        List<Client> clients = clientRepository.findAll();

        if (clients.isEmpty()) {
            throw new NotFoundException("No client list found");
        }

        return clients;
    }

    @Override
    public Page<ClientListResponse> getUniqueClient(Pageable pageable, Integer requestPage) {
        Page<ClientListResponse> clients = clientRepository.findAllUniqueClients(pageable, requestPage);

        checkOutOfPage.isInvalidPage(clients.getTotalPages(), requestPage);

        if (clients.isEmpty()) {
            throw new NotFoundException("No client list found");
        }

        return clients;
    }

    @Override
    public Page<ClientListResponse> searchClientRequestByEmail(Pageable pageable, Integer requestPage, String email) {
        Page<ClientListResponse> clients = clientRepository.findClientRequestByEmail(email, pageable, requestPage);

        checkOutOfPage.isInvalidPage(clients.getTotalPages(), requestPage);

        if (clients.isEmpty()) {
            throw new NotFoundException("No client list found");
        }

        return clients;
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client id " + clientId + " not found."));
    }

    @Override
    public ClientResponse createNewClient(ClientRequest request) throws MessagingException {
        Client client = request.toEntity();
        mapRequestToClient(client, request);

        Client savedClient = clientRepository.save(client);

        sendClientStatusEmail(
                savedClient.getEmail(),
                savedClient.getClientName(),
                savedClient.getClientImage(),
                savedClient.getStatus(),
                savedClient.getComplaint(),
                savedClient.getFeedBack(),
                savedClient.getClientId(),
                savedClient.getCreatedAt(),
                savedClient.getUpdatedAt()
        );

        return savedClient.toResponse();
    }

    @Override
    public ClientResponse modifiedClientById(ClientRequest clientRequest, Long clientId) throws MessagingException {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        mapRequestToClient(existingClient, clientRequest);

        Client updatedClient = clientRepository.save(existingClient);

        sendClientStatusEmail(
                updatedClient.getEmail(),
                updatedClient.getClientName(),
                updatedClient.getClientImage(),
                updatedClient.getStatus(),
                updatedClient.getComplaint(),
                updatedClient.getFeedBack(),
                updatedClient.getClientId(),
                updatedClient.getCreatedAt(),
                updatedClient.getCreatedAt()
        );

        return updatedClient.toResponse();
    }

    @Override
    public Void removeClientById(Long clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        } else {
            throw new NotFoundException("Client id " + clientId + " not found.");
        }
        return null;
    }

    private void mapRequestToClient(Client client, ClientRequest request) {
        client.setClientName(request.getClientName());
        client.setEmail(normalizeEmail(request.getEmail()));
        client.setStatus(request.getStatus());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setAddress(request.getAddress());
        client.setComplaint(request.getComplaint());
        client.setFeedBack(request.getFeedBack());
        client.setClientImage(request.getClientImage());
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private void sendClientStatusEmail(
            String toEmail,
            String clientName,
            String clientImage,
            ClientStatus status,
            String complaint,
            String feedBack,
            Long clientId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt

    ) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariable("subject", getEmailSubject(status));
        context.setVariable("title", getEmailTitle(status));
        context.setVariable("user", clientName);
        context.setVariable("clientEmail", toEmail);
        context.setVariable("requestStatus", status);
        context.setVariable("message", getEmailMessage(status, clientName));
        context.setVariable("caseLink", buildCaseLink(clientId));
        context.setVariable("complaint", complaint);
        context.setVariable("feedBack" , feedBack);
        context.setVariable("clientImage", clientImage);
        context.setVariable("createdAt" , createdAt);
        context.setVariable("updatedAt" , updatedAt);

        String htmlContent = templateEngine.process("notification-template", context);

        helper.setSubject(getEmailSubject(status));
        helper.setTo(toEmail);
        helper.setFrom(adminEmail);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    private String buildCaseLink(Long clientId) {
        return "https://your-lawfirm.com/cases/" + clientId;
    }

    private String getEmailSubject(ClientStatus status) {
        return switch (status) {
            case PENDING -> "We Have Received Your Request";
            case REJECTED -> "Update Regarding Your Request";
            case APPROVED -> "Your Request Has Been Approved";
            case IN_PROGRESS -> "Your Request Is In Progress";
            case DONE -> "Your Request Has Been Completed";
        };
    }

    private String getEmailTitle(ClientStatus status) {
        return switch (status) {
            case PENDING -> "Request Received";
            case REJECTED -> "Request Update";
            case APPROVED -> "Request Approved";
            case IN_PROGRESS -> "Request In Progress";
            case DONE -> "Request Completed";
        };
    }

    private String getEmailMessage(ClientStatus status, String clientName) {
        return switch (status) {
            case PENDING -> """

                    Thank you for contacting CG Law Firm. We have successfully received your request.
                    Our team will review your case carefully and get back to you as soon as possible.<br><br>
                    We appreciate your patience and trust in our service.
                    """.formatted(clientName);

            case REJECTED -> """
              
                    Thank you for reaching out to CG Law Firm.
                    After careful review, we regret to inform you that we are unable to proceed with your request at this time.<br><br>
                    We sincerely appreciate your understanding and thank you for considering our firm.
                    """.formatted(clientName);

            case APPROVED -> """
                    
                    We are pleased to inform you that your request has been approved.
                    Our legal team will proceed with the next steps and contact you if additional information is needed.<br><br>
                    Thank you for your trust in CG Law Firm.
                    """.formatted(clientName);

            case IN_PROGRESS -> """
                    
                    We would like to inform you that your request is currently being processed by our legal team.
                    We are actively working on your case and will keep you updated on important progress.<br><br>
                    Thank you for your patience and continued confidence in CG Law Firm.
                    """.formatted(clientName);

            case DONE -> """
                  
                    We are pleased to let you know that your request has been completed.
                    Thank you for allowing CG Law Firm to assist you throughout this process.<br><br>
                    Should you require any further legal assistance in the future, we would be happy to help.
                    """.formatted(clientName);
        };
    }
}