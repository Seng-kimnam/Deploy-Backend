package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.doc.request.DocRequest;
import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Document;
import _bbu.lawfirmapi.services.doc.DocService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController  extends BaseResponse {

    private final DocService docService;
    
    @GetMapping("/all-docs")
    public ResponseEntity<ApiResponse<Page<DocResponse>>> getDocsWithPagination(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "docId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1 , size , sort);
        Page<DocResponse> docList = docService.fetchDocWithPagination(pageable ,page );
        return responseEntity(true ,
                "Get all document successfully",
                HttpStatus.OK,
                docList);
    }
    @GetMapping("/without-pagination")
    public ResponseEntity<ApiResponse<List<DocResponse>>> getAllDocuments(){
        return responseEntity(true ,
                "Get all document successfully",
                HttpStatus.OK,
                docService.fetchAllDocs());
    }
//    @SecurityRequirement(name = "bearerAuth")

    @GetMapping("/{documentId:\\d+}")
    public ResponseEntity<ApiResponse<DocResponse>> getDocById(@PathVariable @Valid @Positive Long documentId){
        return responseEntity(true ,
                "Get document with id " + documentId + " successfully",
                HttpStatus.ACCEPTED,
                docService.fetchDocById(documentId));
    }

    @GetMapping("/filter-by-category")
    public ResponseEntity<ApiResponse<List<DocResponse>>> getDocListWithCategory(@RequestParam String  categoryName){
        return responseEntity(true ,
                "Get document with category " + categoryName.toUpperCase() + " successfully",
                HttpStatus.ACCEPTED,
                docService.fetchDocsByCategoryName(categoryName.toUpperCase()));
    } @GetMapping("/search-document")
    public ResponseEntity<ApiResponse<List<DocResponse>>> searchDocumentByKeyword(
            @RequestParam String keyword,
            @RequestParam(required = false) String categoryName){
        return responseEntity(true ,
                "Search document with category "  + " and keyword " + keyword + " successfully",
                HttpStatus.ACCEPTED,
                docService.fetchDocByKeyword(keyword , categoryName));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<DocResponse>> insertNewDoc(@RequestBody DocRequest docRequest){
        return responseEntity(true ,
                "Create new document successfully",
                HttpStatus.CREATED,
                docService.createNewDocument(docRequest));
    }


    @PutMapping("/{documentId}")
    public ResponseEntity<ApiResponse<DocResponse>> updateDocById(
            @PathVariable @Valid @Positive Long documentId ,
            @RequestBody DocRequest docRequest
    ) {
        return responseEntity(true ,
                "Update document successfully",
                HttpStatus.ACCEPTED,
                docService.modifiedExistDocumentById(documentId , docRequest));
    }


    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDocById(@PathVariable @Valid @Positive Long documentId){
        return responseEntity(true ,
                "Delete document "+ docService.fetchDocById(documentId).getTitle()  + " successfully",
                HttpStatus.OK,
                docService.removeExistingDocumentById(documentId));
    }
}