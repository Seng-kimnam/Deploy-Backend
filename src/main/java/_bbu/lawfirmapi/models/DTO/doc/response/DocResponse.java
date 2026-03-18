package _bbu.lawfirmapi.models.DTO.doc.response;


import _bbu.lawfirmapi.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocResponse  {
    private Long docId;

    private String title;

    private String fileCover;

    private String fileUrl;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
