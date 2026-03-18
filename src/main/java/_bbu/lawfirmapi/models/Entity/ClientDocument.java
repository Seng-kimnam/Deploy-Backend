package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_documents")
@JsonPropertyOrder({"id" , "clientId", "document" , "description" , "createdAt" , "updatedAt"})
public class ClientDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "documents", columnDefinition = "jsonb")
    private List<Map<String, String>> documents;

    @Column(name = "description" )
    private String description;


}
