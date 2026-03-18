package _bbu.lawfirmapi.repositories;

import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.models.Entity.Document;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository< Document , Long> , JpaSpecificationExecutor<Document> {
    @Query("""
    SELECT new _bbu.lawfirmapi.models.DTO.doc.response.DocResponse(
        d.docId,
        d.title,
        d.fileCover,
        d.fileUrl,
        d.category.categoryName,
        d.createdAt,
        d.updatedAt
    )
    FROM Document d
    WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(d.category.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))
""")
    List<DocResponse> searchDocs(@Param("keyword") String keyword ,@Param("categoryName") String categoryName  );


    @Query("""
        SELECT d FROM Document d
        JOIN d.appUser u
        WHERE u.email = :email
    """)
    Page<Document> findDocumentsByLawyerEmail(
            @Param("email") String email,
            Pageable pageable
    );
    @Query("""
        SELECT d FROM Document d
        JOIN d.appUser u
        WHERE d.docId = :id AND u.email = :email
    """)
    Optional<Document> findByIdAndLawyerEmail(
            @Param("id") Long id,
            @Param("email") String email
    );


}
