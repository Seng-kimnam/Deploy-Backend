package _bbu.lawfirmapi.controllers;

import _bbu.lawfirmapi.models.DTO.category.request.CateRequest;
import _bbu.lawfirmapi.models.DTO.category.response.CateResponse;
import _bbu.lawfirmapi.models.DTO.doc.request.DocRequest;
import _bbu.lawfirmapi.models.DTO.doc.response.DocResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.ApiResponse;
import _bbu.lawfirmapi.models.DTO.shared.response.BaseResponse;
import _bbu.lawfirmapi.models.Entity.Category;
import _bbu.lawfirmapi.models.Entity.Document;
import _bbu.lawfirmapi.services.category.CategoryService;
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
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController  extends BaseResponse {

    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Category>>> getAllCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page -1 , size , sort);
        Page<Category> categoryList = categoryService.fetchAllCategories(pageable , page);

        return responseEntity(true ,
                "Get all categories successfully",
                HttpStatus.OK,
                categoryList);
    }
    @GetMapping("/without-pagination")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories(){
        return responseEntity(true ,
                "Get all categories successfully",
                HttpStatus.OK,
                categoryService.fetchCategoriesWithoutPagination());
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable @Valid @Positive Long categoryId){
        return responseEntity(true ,
                "Get category with id " + categoryId + " successfully",
                HttpStatus.ACCEPTED,
                categoryService.fetchCateById(categoryId));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<CateResponse>> insertNewCategory(@RequestBody CateRequest cateRequest){
        return responseEntity(true ,
                "Create new category successfully",
                HttpStatus.CREATED,
                categoryService.createNewCategory(cateRequest));
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CateResponse>> updateDocById(
            @PathVariable @Valid @Positive Long categoryId ,
            @RequestBody CateRequest cateRequest
    ) {
        return responseEntity(true ,
                "Update document successfully",
                HttpStatus.ACCEPTED,
                categoryService.modifiedExistCategoryById(categoryId , cateRequest));
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoryById(@PathVariable @Valid @Positive Long categoryId){
        return responseEntity(true ,
                "Delete document "+ categoryService.fetchCateById(categoryId).getCategoryName()  + " successfully",
                HttpStatus.ACCEPTED,
                categoryService.removeExistingCategoryById(categoryId));
    }
}
