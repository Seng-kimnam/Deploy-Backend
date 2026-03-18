package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.MonthlyStatistic;
import _bbu.lawfirmapi.models.DTO.client.request.ClientRequest;
import _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse;
import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.Entity.Client;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client , Long> {

    @Query("""
        SELECT MONTH(c.createdAt) , COUNT (c)
            FROM Client c
                WHERE YEAR(c.createdAt) = :year
                    GROUP BY MONTH (c.createdAt)
    """)
    List<Object[]> getMonthlyStatistic(@Param("year") int year);

    @Query("""
    SELECT new _bbu.lawfirmapi.models.DTO.MonthlyStatistic(
        MONTH(c.createdAt),
        COUNT(c)
    )
    FROM Client c
    GROUP BY MONTH(c.createdAt)
""")
    List<MonthlyStatistic> getOnlyMonthlyStatistic();
    @Query("""
    SELECT
        ((MONTH(c.createdAt) - 1) / 3) + 1,
        COUNT(c)
    FROM Client c
    WHERE YEAR(c.createdAt) = :year
    GROUP BY ((MONTH(c.createdAt) - 1) / 3) + 1
    ORDER BY ((MONTH(c.createdAt) - 1) / 3) + 1
""")
    List<Object[]> getQuarterlyStatistic(@Param("year") int year);

    @Query(value = """
        SELECT EXTRACT(YEAR FROM c.createdAt) , COUNT(c)
            FROM Client c
            WHERE c.createdAt IS NOT NULL
                    GROUP BY EXTRACT(YEAR FROM c.createdAt)
                        ORDER BY EXTRACT(YEAR FROM c.createdAt)
    """)
    List<Object[]> getAnnualStatistic();

    @Query("""
    SELECT new _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse(
   
        c.email,
        MAX(c.clientName),
        COUNT(c.clientId)
    )
    FROM Client c
    GROUP BY c.email
""")
    Page<ClientListResponse> findAllUniqueClients(Pageable pageable , Integer requestPage);

    @Query(
            value = """
        SELECT new _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse(
            c.email,
            MAX(c.clientName),
            COUNT(c.clientId)
        )
        FROM Client c
        WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
        GROUP BY c.email
    """,
            countQuery = """
        SELECT COUNT(DISTINCT c.email)
        FROM Client c
        WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
    """
    )
    Page<ClientListResponse> findClientRequestByEmail(
            @Param("email") String email,
            Pageable pageable,
            Integer requestedPage
    );

    @Query("""
    SELECT c FROM Client c
    WHERE LOWER(c.clientName) LIKE LOWER(CONCAT('%' ,:keyword  ,'%'))
    OR LOWER(c.email) LIKE LOWER(CONCAT('%' , :keyword , '%'))
    OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%' , :keyword , '%'))
    OR LOWER(c.address) LIKE LOWER(CONCAT('%' , :keyword , '%'))
    OR LOWER(c.complaint) LIKE LOWER(CONCAT('%' , :keyword , '%'))
""")
    Page<Client> searchClients(@Param("keyword") String keyword , Pageable pageable);
    @Query("""
    SELECT c FROM Client c
    WHERE LOWER(c.clientName) LIKE LOWER(CONCAT('%' ,:keyword  ,'%'))
    AND LOWER(c.email) LIKE LOWER(CONCAT('%' , :email , '%'))
    OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%' , :keyword , '%'))
    OR LOWER(c.address) LIKE LOWER(CONCAT('%' , :keyword , '%'))
    OR LOWER(c.complaint) LIKE LOWER(CONCAT('%' , :keyword , '%'))
""")
    Page<Client> searchDetailClientRequest(@Param("keyword") String keyword , @Param("email") String email, Pageable pageable);

    Page<Client> findByEmail(Pageable pageable, String email);

    @Query(
            value = """
        SELECT new _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse(
            c.email,
            MAX(c.clientName),
            COUNT(c.clientId)
        )
        FROM Client c
        WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
        GROUP BY c.email
    """,
            countQuery = """
        SELECT COUNT(DISTINCT c.email)
        FROM Client c
        WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
    """
    )
    ClientListResponse findUniqueClientByEmail(
            @Param("email") String email

    );
}
