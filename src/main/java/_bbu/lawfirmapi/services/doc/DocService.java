package _bbu.lawfirmapi.services.doc;

import _bbu.lawfirmapi.models.DTO.doc.request.DocRequest;
import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.models.Entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocService {

    Page<DocResponse> fetchDocWithPagination(Pageable pageable , Integer requestPage);
    List<DocResponse> fetchAllDocs();
    List<DocResponse> fetchDocsByCategoryName(String categoryName);
    List<DocResponse> fetchDocByKeyword(String keyword , String categoryName);
    DocResponse fetchDocById(Long docId);
    DocResponse createNewDocument(DocRequest docRequest);
    DocResponse modifiedExistDocumentById( Long docId, DocRequest docRequest);
    Void removeExistingDocumentById(Long docId);

}
