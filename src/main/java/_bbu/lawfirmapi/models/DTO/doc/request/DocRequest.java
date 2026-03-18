package _bbu.lawfirmapi.models.DTO.doc.request;

import _bbu.lawfirmapi.models.Entity.Document;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DocRequest {

    private String title;
    private String fileCover;
    private String fileUrl;
    private Long categoryId;

    public Document toEntity(){
        return new Document(
                null,
                this.title,
                this.fileCover,
                this.fileUrl,
                this.categoryId
        );
    }
}
