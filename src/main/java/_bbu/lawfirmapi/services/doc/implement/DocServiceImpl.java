package _bbu.lawfirmapi.services.doc.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.doc.request.DocRequest;
import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.models.Entity.AppUser;
import _bbu.lawfirmapi.models.Entity.Category;
import _bbu.lawfirmapi.models.Entity.Document;
import _bbu.lawfirmapi.repositories.AppUserRepository;
import _bbu.lawfirmapi.repositories.CategoryRepository;
import _bbu.lawfirmapi.repositories.DocumentRepository;
import _bbu.lawfirmapi.services.doc.DocService;
import _bbu.lawfirmapi.utils.MethodHelper;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService  {
    private final DocumentRepository documentRepo;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepo;
    private final MethodHelper methodHelper;



    public AppUser getCurrentAdminEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Unauthenticated");
        }

        String email = auth.getName();
        AppUser currentProfile =  appUserRepo.findAppUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return currentProfile;
    }
    @Override
    public List<DocResponse> fetchAllDocs() {
        if(documentRepo.findAll().isEmpty()){
            throw  new NotFoundException("List document not found");
        }
        List<DocResponse> listOfDocs = documentRepo.findAll().stream().map(
                doc -> new DocResponse(
                        doc.getDocId(),
                        doc.getTitle(),
                        doc.getFileCover(),
                        doc.getFileUrl(),
                        doc.getCategory().getCategoryName(),
                        doc.getCreatedAt(),
                        doc.getUpdatedAt()
                )
        ).toList();
        return listOfDocs;
    }
    public class DocumentSpecs {

    }

    @Override
    public Page<DocResponse> fetchDocWithPagination(Pageable pageable, Integer page) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Page<Document> docs;

        if (methodHelper.isAdmin(auth)) {
            docs = documentRepo.findAll(pageable);
        } else if (methodHelper.isLawyer(auth)) {
            docs = documentRepo.findDocumentsByLawyerEmail(auth.getName(), pageable);
        } else {
            throw new AccessDeniedException("Access denied");
        }

        methodHelper.isInvalidPage(docs.getTotalPages(), page);

        if (docs.isEmpty()) {
            throw new NotFoundException("No documents found");
        }

        return docs.map(Document::toResponse);
    }


    @Override
    public List<DocResponse> fetchDocsByCategoryName(String categoryName){
        List<DocResponse> docsByCateName = documentRepo.findAll(methodHelper.categoryNameContains(categoryName.toUpperCase()))
                .stream()
                .map(
                        doc -> new DocResponse(
                                doc.getDocId(),
                                doc.getTitle(),
                                doc.getFileCover(),
                                doc.getFileUrl(),
                                doc.getCategory().getCategoryName(),
                                doc.getCreatedAt(),
                                doc.getUpdatedAt()
                        )
                ).toList();


        return docsByCateName;
    }
    @Override
    public List<DocResponse> fetchDocByKeyword(String keyword , String categoryName){
        List<DocResponse> listDocs = documentRepo.searchDocs(keyword , categoryName);
        if(listDocs.isEmpty()){
            throw new NotFoundException("Document with category name "  + "categoryName " + categoryName + " and keyword " + keyword +" Not found.");
        }

        return listDocs;
    }

    @Override
    public DocResponse fetchDocById(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Document doc;

        if (methodHelper.isAdmin(auth)) {
            doc = documentRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Document not found"));
        } else if (methodHelper.isLawyer(auth)) {
            doc = documentRepo.findByIdAndLawyerEmail(id, auth.getName())
                    .orElseThrow(() -> new NotFoundException("Document not found"));
        } else {
            throw new AccessDeniedException("Access denied");
        }

        return doc.toResponse();
    }

    @Override
    public DocResponse createNewDocument(DocRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (!methodHelper.isLawyer(auth) && !methodHelper.isAdmin(auth)) {
            throw new AccessDeniedException("Only lawyers and Admin can upload documents");
        }

        AppUser lawyer = appUserRepo.findAppUserByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Document doc = new Document();
        doc.setTitle(request.getTitle());
        doc.setFileCover(request.getFileCover());
        doc.setFileUrl(request.getFileUrl());
        doc.setCategory(category);
        doc.setAppUser(lawyer);

        return documentRepo.save(doc).toResponse();
    }


    @Override
    public DocResponse modifiedExistDocumentById( Long docId, DocRequest docRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (!methodHelper.isLawyer(auth) && !methodHelper.isAdmin(auth)) {
            throw new AccessDeniedException("Only lawyers and Admin can upload documents");
        }

        AppUser lawyer = appUserRepo.findAppUserByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document currentDoc =  documentRepo.findById(docId).orElseThrow(
                () -> new NotFoundException("Document with id " + docId +  " not found.")
        );
        Category category = categoryRepository.findById(docRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));



        currentDoc.setTitle(docRequest.getTitle());
        currentDoc.setFileCover(docRequest.getFileCover());
        currentDoc.setFileUrl(docRequest.getFileUrl());
        currentDoc.setCategory(category);
        currentDoc.setUpdatedAt(LocalDateTime.now());
        currentDoc.setAppUser(lawyer);
        DocResponse saveUpdateDoc = documentRepo.save(currentDoc).toResponse();
        return saveUpdateDoc;
    }

    @Override
    public Void removeExistingDocumentById(Long docId) {
        documentRepo.findById(docId).orElseThrow(
                () -> new NotFoundException("Document with id " + docId +  " not found.")
        );
        documentRepo.deleteById(docId);
        return null;
    }
}