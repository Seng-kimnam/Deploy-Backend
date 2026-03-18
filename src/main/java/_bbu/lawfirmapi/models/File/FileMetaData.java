package _bbu.lawfirmapi.models.File;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileMetaData {
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
}
