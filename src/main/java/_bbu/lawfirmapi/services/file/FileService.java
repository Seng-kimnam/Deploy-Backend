package _bbu.lawfirmapi.services.file;

import _bbu.lawfirmapi.models.File.FileMetaData;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileService {

    String uploadPdfFile(MultipartFile file , String lawType) throws Exception;
    List<String> uploadMultipleFilePdf(List<MultipartFile> files) throws  Exception;
    List<String> filterFileByLawType(String lawType) throws Exception;
    String getPdfPreviewUrl(String objectName) throws  Exception;
    FileMetaData uploadFile(MultipartFile file);
    List<String> getBannerImagesList();
    FileMetaData uploadBannerImages(MultipartFile file) throws  Exception ;
    InputStream getFileByFileName(String fileName);

    List<FileMetaData> bulkUploadFile(List<MultipartFile> files);

    List<String> getAllImagesUrl();

    Void deleteBannerByName(String posterName);

    FileMetaData editBannerByName(String oldBannerName, MultipartFile newFile) throws Exception;

    // last update methods of thesis

    Object uploadClientDocuments(Long clientId, List<MultipartFile> files, String description) throws Exception;
    Object getClientDocuments(Long clientId);
    Object getAllClientDocuments(String keyword);
    Object searchClientDocuments(String keyword);
}
