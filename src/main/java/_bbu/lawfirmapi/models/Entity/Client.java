package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.Enumerations.ClientStatus;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({
        "clientId" , "clientName" , "email", "phoneNumber"  ,"complaint" ,"address" ,"status" , "clientImage" , "createdAt" , "updatedAt"
})
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "client_id")
    private Long  clientId ;
    @Column(name = "client_name")
    private String clientName ;
    @Column(name = "email" , unique = true)
    private String email ;
    @Column(name = "phone_number" , unique = true)
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name =  "complaint" , columnDefinition = "TEXT")
    private String complaint ;
    @Enumerated(EnumType.STRING)
    @Column(name = "status" , columnDefinition = "VARCHAR(20)")
    private ClientStatus status;
    @Column(name = "client_image")
    private String clientImage;
    @Column(name = "feed_back" )
    private String feedBack;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL)
    // "client"that map by id field name in table which has relation with
    @JsonIgnore
    @ToString.Exclude
    private List<Case> cases;

//    private Long requestCount;


    public Client(Object o, String clientName, String email, ClientStatus status, String phoneNumber, String address, String complaint, String feedBack,  String clientImage ) {
    }

    public ClientResponse toResponse(){
        return new ClientResponse(
                this.clientId ,
                this.clientName ,
                this.email ,
                this.status,
                this.phoneNumber ,
                this.address ,
                this.complaint ,
                this.clientImage ,
                this.feedBack,
                this.getCreatedAt() ,
                this.getUpdatedAt());
    }
}
