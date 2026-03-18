package _bbu.lawfirmapi.services.file.implement;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import _bbu.lawfirmapi.exceptions.InvalidException;
import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.models.DTO.client.response.ClientListResponse;
import _bbu.lawfirmapi.models.DTO.client.response.ClientResponse;
import _bbu.lawfirmapi.models.Entity.Client;
import _bbu.lawfirmapi.models.File.FileMetaData;
import _bbu.lawfirmapi.models.Entity.ClientDocument;
import _bbu.lawfirmapi.repositories.ClientDocumentRepository;
import _bbu.lawfirmapi.repositories.ClientRepository;
import _bbu.lawfirmapi.services.file.FileService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class FileServiceImplement implements FileService {
    private final MinioClient minioClient;
    private final ClientDocumentRepository clientDocumentRepository;
    private final ClientRepository clientRepo;

    @Value("${minio.bucket.name}")
    private String bucketName;
    @Value("${minio.url}")
    private String minioUrl;
    private void verifyFileExtension(MultipartFile file) {
        // validate file extension allow only ending with .png, .svg, .jpg, .jpeg, .gif, .webp or .pdf
        List<String> allowFileExtensions =
                List.of("image/png", "image/svg+xml", "image/jpg", "image/jpeg", "image/gif", "image/webp", "application/pdf");

        if (!allowFileExtensions.contains(file.getContentType()) || file.getContentType() == null) {
            throw new InvalidException(
                    "Profile image must be a valid image URL ending with .png, .svg, .jpg, .jpeg, .gif, .webp or .pdf");
        }
    }

    @SneakyThrows
    private FileMetaData uploadFileToMinio(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);

        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .contentType(file.getContentType()).stream(file.getInputStream(), file.getSize(), -1)
                .build());

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/preview-file")
                .queryParam("fileName" , fileName)
                .toUriString();

        return FileMetaData.builder().fileName(fileName).fileType(file.getContentType())
                .fileUrl(fileUrl).fileSize(file.getSize()).build();
    }

    @SneakyThrows
    @Override
    public FileMetaData uploadFile(MultipartFile file) {
        verifyFileExtension(file);

        boolean bucketExists =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        return uploadFileToMinio(file);
    }

    @SneakyThrows
    @Override
    public InputStream getFileByFileName(String fileName ) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name must not be null or empty");
        }
        if (fileName.startsWith("http")) {
            throw new IllegalArgumentException("Invalid file name: URL not allowed. Use the filename returned from upload.");
        }
        return minioClient
                .getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }
    @SneakyThrows
    @Override
    public List<FileMetaData> bulkUploadFile(List<MultipartFile> files) {
        for (MultipartFile multipartFile : files) {
            verifyFileExtension(multipartFile);
        }

        List<FileMetaData> responseFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            responseFiles.add(uploadFileToMinio(file));
        }
        return responseFiles;
    }

    @Override
    @SneakyThrows
    public List<String>  getBannerImagesList(){
        List<String> objectNames = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix("Banner/")  // Filter by law type prefix
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            objectNames.add(item.objectName());
        }
        if(objectNames.isEmpty()){
            throw  new NotFoundException("No poster banner found.");
        }

        return objectNames;
    }
    @Override
    @SneakyThrows
    public FileMetaData uploadBannerImages(MultipartFile file) throws Exception {

        // 1. Prefix (folder)
        String prefix = "Banner/";

        // 2. Get extension safely
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // 3. Generate unique filename
        String fileName = prefix + UUID.randomUUID() + "." + extension;

        // 4. Upload to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        // 5. Preview URL (your existing API)
        String fileUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/files/preview-file")
                .queryParam("fileName" , fileName)
                .toUriString();

        // 6. Return metadata
        return FileMetaData.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .build();
    }

    @SneakyThrows
    public List<String> getAllImagesUrl(){
        List<String> listOfUrls = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build());
        for(Result<Item> result : results ){
            Item item = result.get();
            String fileName = item.objectName();
            String url = "http://localhost:9000/" + bucketName + "/" + fileName;;
            listOfUrls.add(url);
        }
        List<String> pdfOnly = listOfUrls.stream()
                .filter(url -> url.toLowerCase().endsWith(".pdf")).collect(Collectors.toList());

        return pdfOnly;
    }

    @Override
    public List<String> uploadMultipleFilePdf(List<MultipartFile> files) throws Exception {
        List<String> objectNames = new ArrayList<>();

        for(MultipartFile file : files){
            // Generate unique name to avoid collisions
            String objectName = UUID.randomUUID().toString() + ".pdf";

            // Upload each file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            objectNames.add(objectName);

        }
        return objectNames;
    }

    @Override
    public List<String> filterFileByLawType(String lawType) throws  Exception{
        List<String> objectNames = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(lawType + "/")  // Filter by law type prefix
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            objectNames.add(item.objectName());
        }

        return objectNames;
    }
//    @Override
//    public String uploadPdfFile(MultipartFile file) throws Exception {
//        String objectName = UUID.randomUUID().toString()+".pdf";
//
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .stream(file.getInputStream(), file.getSize(), -1)
//                        .contentType(file.getContentType()) // Sets content type
//                        .build());
//
//        return objectName;
//    }
    @Override
public String uploadPdfFile(MultipartFile file, String lawType) throws Exception {
    // 1. Dynamic prefix from law type enum
    String lawTypePrefix = lawType;  // e.g., CRIMINAL/, BANK_AND_FINANCE/

    // 2. Timestamp for uniqueness
    String timestamp = String.valueOf(System.currentTimeMillis());

    // 3. Optional: keep original file name
    String originalName = file.getOriginalFilename();

    // 4. Build object name
    String objectName = lawTypePrefix + timestamp + "_" + originalName;

    // 5. Upload to MinIO
    minioClient.putObject(
            PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
    );

    return objectName;
}


    @Override
    public String getPdfPreviewUrl(String fileName) throws Exception {
        // Set response-content-type to application/pdf for inline browser preview
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-type", "application/pdf");

        // Generate a temporary presigned URL that expires in a short time (e.g., 1 hour)
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(fileName)
                        .expiry(1, TimeUnit.HOURS)
                        .extraQueryParams(reqParams)
                        .build());
        return url;
    }
    @Override
    @SneakyThrows
    public Void deleteBannerByName(String posterName){
        if (posterName == null || !posterName.startsWith("Banner/")) {
            throw new IllegalArgumentException("Invalid banner path");
        }
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(posterName)
                        .build()
        );
        return null;
    }
    @Override
    @SneakyThrows
    public FileMetaData editBannerByName(String oldBannerName, MultipartFile newFile) throws Exception {
        if (oldBannerName == null || oldBannerName.isBlank()) {
            throw new IllegalArgumentException("Old banner name is required");
        }

        deleteBannerByName(oldBannerName);

        return uploadBannerImages(newFile);
    }

    // last update code of thesis
    @Override
    public Object uploadClientDocuments(Long clientId, List<MultipartFile> files, String description) throws Exception {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files are required");
        }
        Client clientWhoseUpload = clientRepo.findById(clientId)
                .orElseThrow(() -> new NotFoundException("client request with id " + clientId));

        ClientListResponse clientListResponse = clientRepo.findUniqueClientByEmail(clientWhoseUpload.getEmail());
        String clientName = clientListResponse.getClientName();
        List<Map<String, String>> documentList = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = clientName + "-" + clientId + "-" + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/preview-file")
                    .queryParam("fileName" , fileName)
                    .toUriString();

            Map<String, String> docInfo = new HashMap<>();
            docInfo.put("name", file.getOriginalFilename());
            docInfo.put("fileName", fileName);
            docInfo.put("fileUrl", fileUrl);
            docInfo.put("fileType", file.getContentType());
            docInfo.put("fileSize", String.valueOf(file.getSize()));
            documentList.add(docInfo);
        }

        Optional<ClientDocument> existingDoc = clientDocumentRepository.findByClientId(clientId);
        ClientDocument clientDocument;

        if (existingDoc.isPresent()) {
            clientDocument = existingDoc.get();
            clientDocument.getDocuments().addAll(documentList);
        } else {
            clientDocument = new ClientDocument();
            clientDocument.setClientId(clientId);
            clientDocument.setDocuments(documentList);
        }

        if (description != null && !description.isBlank()) {
            clientDocument.setDescription(description);
        }

        return clientDocumentRepository.save(clientDocument);
    }

    @Override
    public Object getClientDocuments(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        return clientDocumentRepository.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundException("No documents found for client ID: " + clientId));
    }

    @Override
    public Object getAllClientDocuments(String keyword) {
        List<ClientDocument> allDocs = clientDocumentRepository.findAll();

        if (allDocs.isEmpty()) {
            throw new NotFoundException("No client documents found");
        }

        String normalizedKeyword = keyword != null ? keyword.trim().toLowerCase() : "";
        List<Map<String, Object>> result = new ArrayList<>();
        Set<Long> seenClientIds = new HashSet<>();

        for (ClientDocument doc : allDocs) {
            if (seenClientIds.contains(doc.getClientId())) {
                continue;
            }

            Client client = clientRepo.findById(doc.getClientId()).orElse(null);
            String clientName = client != null && client.getClientName() != null
                    ? client.getClientName()
                    : "";

            boolean matchesSearch = normalizedKeyword.isBlank()
                    || clientName.toLowerCase().contains(normalizedKeyword)
                    || hasMatchingDocumentName(doc.getDocuments(), normalizedKeyword);

            if (!matchesSearch) {
                continue;
            }

            seenClientIds.add(doc.getClientId());

            Map<String, Object> docInfo = new HashMap<>();
            docInfo.put("id", doc.getId());
            docInfo.put("clientId", doc.getClientId());
            docInfo.put("clientName", clientName);
            docInfo.put("clientEmail", client != null ? client.getEmail() : "");
            docInfo.put("description", doc.getDescription());
            docInfo.put("documents", doc.getDocuments());
            docInfo.put("createdAt", doc.getCreatedAt());
            docInfo.put("updatedAt", doc.getUpdatedAt());
            result.add(docInfo);
        }

        if (result.isEmpty()) {
            throw new NotFoundException("No client documents found matching: " + keyword);
        }

        return result;
    }

    private boolean hasMatchingDocumentName(List<Map<String, String>> documents, String keyword) {
        if (documents == null || keyword.isBlank()) {
            return false;
        }

        for (Map<String, String> document : documents) {
            String documentName = document.get("name");
            if (documentName != null && documentName.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object searchClientDocuments(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword is required");
        }

        List<ClientDocument> allDocs = clientDocumentRepository.findAll();
        
        if (allDocs.isEmpty()) {
            throw new NotFoundException("No documents found");
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Map<String, String>> matchingFiles = new ArrayList<>();
        
        for (ClientDocument doc : allDocs) {
            Client client = clientRepo.findById(doc.getClientId()).orElse(null);
            String clientName = (client != null && client.getClientName() != null) 
                ? client.getClientName().toLowerCase() 
                : "";
            
            if (doc.getDocuments() != null) {
                for (Map<String, String> file : doc.getDocuments()) {
                    String fileName = file.get("name") != null ? file.get("name").toLowerCase() : "";
                    if (clientName.contains(lowerKeyword) || fileName.contains(lowerKeyword)) {
                        Map<String, String> matchedFile = new HashMap<>(file);
                        matchedFile.put("id" , doc.getId().toString());
                        matchedFile.put("clientId", doc.getClientId().toString());
                        matchedFile.put("clientName", client != null ? client.getClientName() : "");
                        matchedFile.put("description" , doc.getDescription());
                        matchedFile.put("createdAt" , doc.getCreatedAt().toString());
                        matchedFile.put("updatedAt" , doc.getUpdatedAt().toString());
                        matchingFiles.add(matchedFile);
                    }
                }
            }
        }

        if (matchingFiles.isEmpty()) {
            throw new NotFoundException("No documents found matching: " + keyword);
        }
        
        return matchingFiles;
    }
}
