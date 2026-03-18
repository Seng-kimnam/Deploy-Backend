package _bbu.lawfirmapi.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.File.FileMetaData;
import _bbu.lawfirmapi.services.file.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController extends BaseResponse {
    private final FileService fileService;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getPdfFile(
            @PathVariable String fileName,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "appointmentId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) throws IOException {
        FileSystemResource file = new FileSystemResource("uploads/" + fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetaData>> uploadFile(
            @RequestParam("file") MultipartFile file) {

        ApiResponse<FileMetaData> apiResponse = ApiResponse.<FileMetaData>builder().success(true)
                .message("File uploaded successfully! Metadata of the uploaded file is returned.")
                .status(HttpStatus.CREATED).code(HttpStatus.OK.value())
                .payload(fileService.uploadFile(file)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping(value = "/upload-file/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileMetaData>>> bulkUploadFile(
            @RequestParam("files") List<MultipartFile> files) {

        List<FileMetaData> uploadedFiles = fileService.bulkUploadFile(files);

        ApiResponse<List<FileMetaData>> apiResponse =
                ApiResponse.<List<FileMetaData>>builder().success(true)
                        .message("File uploaded successfully! Metadata of the uploaded file is returned.")
                        .status(HttpStatus.CREATED).code(HttpStatus.OK.value()).payload(uploadedFiles).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @SneakyThrows
    @GetMapping("/preview-file")
    public ResponseEntity<byte[]> getFileByFileName(@RequestParam(required = false) String fileName) {

        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.badRequest().body("File name is required".getBytes());
        }

        if (fileName.equalsIgnoreCase("default-avatar.jpg") || 
            fileName.equalsIgnoreCase("default-avatar.png")) {
            return ResponseEntity.notFound().build();
        }

        InputStream inputStream = fileService.getFileByFileName(fileName);


        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (fileName.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".svg")) {
            mediaType = MediaType.valueOf("image/svg+xml");
        } else if (fileName.endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType)
                .body(inputStream.readAllBytes());
    }
    @PostMapping(value = "/upload-pdf/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultiPdfFile(@RequestParam("file") List<MultipartFile> files) {
        try {

            // Validate each file
            for (MultipartFile file : files) {
                if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                    return ResponseEntity.badRequest().body("Only PDF files are allowed.");
                }
            }

            // Upload all files
            List<String> objectNames = fileService.uploadMultipleFilePdf(files);

            return ResponseEntity.ok(objectNames);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading files: " + e.getMessage());
        }
    }
    @SneakyThrows
    @GetMapping("/download-file/{file-name}")
    public ResponseEntity<byte[]> downloadFileByFileName(@PathVariable("file-name") String fileName){
        InputStream inputStream = fileService.getFileByFileName(fileName);
        byte[] fileBytes = inputStream.readAllBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        MediaType mediaType = fileName.endsWith(".pdf") ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .contentLength(fileBytes.length)
                .body(fileBytes);
    }
    @SecurityRequirement(name = "bearerAuth")
    @SneakyThrows
    @GetMapping("/file-list")
    public ResponseEntity<ApiResponse<List<String>>> getImageList(){
        return responseEntity(true ,
                "Retrieve file list successfully",
                HttpStatus.OK,
                fileService.getAllImagesUrl());
    }

    // ==================================================
    @PostMapping(
            value = "/upload-pdf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadPdf(
            @RequestParam("file-pdf") MultipartFile file,
            @RequestParam String lawType
    ) {
        try {
            if (!"application/pdf".equals(file.getContentType())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Only PDF files are allowed"));
            }

            String objectName = fileService.uploadPdfFile(file, lawType);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "File uploaded successfully",
                            "objectName", objectName
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Endpoint to get a preview URL for a PDF
    @GetMapping("/preview-pdf/{file-name}")
    public ResponseEntity<String> previewPdf(@PathVariable("file-name") String fileName) {
        try {
            String presignedUrl = fileService.getPdfPreviewUrl(fileName);
            // Return the URL to the client, which can then open it in a browser
            return ResponseEntity.ok(presignedUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating preview URL: " + e.getMessage());
        }
    }
    @GetMapping("/law-document")
    public ResponseEntity<ApiResponse<List<String>>> filterDocuments(
            @RequestParam String lawType
    ) throws Exception {
            return responseEntity(
                    true ,
                    "Get law file type " + lawType + " successfully",
                    HttpStatus.ACCEPTED,
                    fileService.filterFileByLawType(lawType)
            );

    }
    @GetMapping("/banner-list")
    public ResponseEntity<ApiResponse<List<String>>> fetchPostList() throws  Exception{
       return responseEntity(
                true ,
                "Get post list successfully",
                HttpStatus.OK,
                fileService.getBannerImagesList()
        );
    }
    @PostMapping( value = "/upload-banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetaData>> insertNewPoster(MultipartFile file) throws  Exception {
        return responseEntity(
                true ,
                "Get new post name " + file.getName() + " successfully",
                HttpStatus.CREATED,
                fileService.uploadBannerImages(file)
        );
    }
    @DeleteMapping("/delete-banner")
    public ResponseEntity<ApiResponse<Void>> removeBannerByName(@RequestParam  String bannerName){
        return responseEntity(
                true ,
                "Delete banner name " + bannerName + " successfully",
                HttpStatus.OK,
                fileService.deleteBannerByName(bannerName)
        );
    }

    @PostMapping(value = "/upload-client-documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> uploadClientDocuments(
            @RequestParam Long clientId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false) String description
    ) throws Exception {
        return responseEntity(
                true,
                "Documents uploaded successfully",
                HttpStatus.CREATED,
                fileService.uploadClientDocuments(clientId, files, description)
        );
    }

    @GetMapping("/client-request-documents/{clientId}")
    public ResponseEntity<ApiResponse<Object>> getClientDocuments(@PathVariable Long clientId) {
        return responseEntity(
                true,
                "Get client documents successfully",
                HttpStatus.OK,
                fileService.getClientDocuments(clientId)
        );
    }

    @GetMapping("/client-documents/all")
    public ResponseEntity<ApiResponse<Object>> getAllClientDocuments(
            @RequestParam(required = false) String keyword
    ) {
        return responseEntity(
                true,
                "Get all client documents successfully",
                HttpStatus.OK,
                fileService.getAllClientDocuments(keyword)
        );
    }

    @GetMapping("/client-documents/search")
    public ResponseEntity<ApiResponse<Object>> searchClientDocuments(
            @RequestParam String keyword
    ) {
        return responseEntity(
                true,
                "Search results for: " + keyword,
                HttpStatus.OK,
                fileService.searchClientDocuments(keyword)
        );
    }

}
