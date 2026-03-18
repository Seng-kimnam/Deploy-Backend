package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.Entity.ClientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientDocumentRepository extends JpaRepository<ClientDocument, Long> {

    Optional<ClientDocument> findByClientId(Long clientId);

    @Query(value = "SELECT * FROM client_documents cd WHERE cd.client_id = :clientId", nativeQuery = true)
    List<ClientDocument> findByClientIdRaw(@Param("clientId") Long clientId);
}
