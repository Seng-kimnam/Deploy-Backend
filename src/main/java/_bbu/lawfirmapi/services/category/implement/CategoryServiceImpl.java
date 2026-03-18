package _bbu.lawfirmapi.services.category.implement;

import _bbu.lawfirmapi.exceptions.NotFoundException;
import _bbu.lawfirmapi.exceptions.ResponseStatusException;
import _bbu.lawfirmapi.models.DTO.category.request.CateRequest;
import _bbu.lawfirmapi.models.DTO.category.response.CateResponse;
import _bbu.lawfirmapi.models.Entity.Category;
import _bbu.lawfirmapi.repositories.CategoryRepository;
import _bbu.lawfirmapi.services.category.CategoryService;
import _bbu.lawfirmapi.utils.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final MethodHelper checkOutOfPage;

    @Override
    public Page<Category> fetchAllCategories(Pageable pageable, Integer requestPage) {
        Page<Category> categories = categoryRepo.findAll(pageable);
        checkOutOfPage.isInvalidPage(categories.getTotalPages() , requestPage);

        if(categories.isEmpty()){
            throw new NotFoundException("No category list found");

        }
        return categories;
    }

    @Override
    public List<Category> fetchCategoriesWithoutPagination(){
        if(categoryRepo.findAll().isEmpty()){
            throw new NotFoundException("No category list found");

        }
        return categoryRepo.findAll();
    }

    @Override
    public Category fetchCateById(Long cateId) {
        return categoryRepo.findById(cateId).orElseThrow(
                () -> new NotFoundException("Category with id " + cateId +  " not found.")
        );
    }

    @Override
    public CateResponse createNewCategory(CateRequest cateRequest) {

        boolean isExistingCategory = categoryRepo.existsByCategoryName(cateRequest.getCategoryName());
        if(isExistingCategory){
            throw new ResponseStatusException(
                    "This category is already exist in the list."
            );
        }

        Category newCategory = cateRequest.toEntity();
        newCategory.setCategoryName(cateRequest.getCategoryName().toUpperCase());
        newCategory.setCreatedAt(LocalDateTime.now());
        CateResponse saveNewCate = categoryRepo.save(newCategory).toResponse();
        return saveNewCate;
    }

    @Override
    public CateResponse modifiedExistCategoryById(Long cateId, CateRequest cateRequest) {
        Category currentCate = categoryRepo.findById(cateId).orElseThrow(
                () -> new NotFoundException("Category with id " + cateId +  " not found.")
        );
        boolean isExistingCategory = categoryRepo.existsByCategoryName(cateRequest.getCategoryName());
        if(isExistingCategory){
            throw new ResponseStatusException(
                    "This category is already exist in the list."
            );
        }
        currentCate.setCategoryName(cateRequest.getCategoryName().toUpperCase());
        currentCate.setUpdatedAt(LocalDateTime.now());
        CateResponse saveUpdateCate = categoryRepo.save(currentCate).toResponse();
        return saveUpdateCate;
    }

    @Override
    public Void removeExistingCategoryById(Long cateId) {
         categoryRepo.findById(cateId).orElseThrow(
                () -> new NotFoundException("Category with id " + cateId +  " not found.")
        );
        categoryRepo.deleteById(cateId);
        return null;
    }
}
