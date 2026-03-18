package _bbu.lawfirmapi.models.Entity;

import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
@JsonPropertyOrder({"docId", "title" , "fileCover" , "fileUrl"})
public class Document extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;
    @Column(name = "title" )
    private String title;
    @Column(name = "file_cover" )
    private String fileCover;
    @Column(name = "file_url" )
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" , referencedColumnName = "category_id")  // this column will be created in documents table
//    @JsonIgnore
    @JsonBackReference
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "appuser_id" , nullable = true)
    private AppUser appUser;
    public Document(Object o, String title, String fileCover, String fileUrl , Long categoryId) {
    }

    public DocResponse toResponse(){
        return new DocResponse(
                this.docId,
                this.title,
                this.fileCover,
                this.fileUrl,
                this.category != null ? this.category.getCategoryName() : null,
                this.getCreatedAt(),
                this.getUpdatedAt()
        );

    }
}
