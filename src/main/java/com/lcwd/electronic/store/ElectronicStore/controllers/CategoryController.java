package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(UrlConstants.BASE_URL)
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @apiNote this is related to create category
     * @author Prashant Bhagat
     * @since V 1.0
     * @param categoryDto
     * @return
     */
    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        log.info("Entering request for create category data");
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        log.info("Completed request for create category data");
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * @apiNote this is related to update category
     * @since V 1.0
     * @author Prashant Bhagat
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId)
    {
        log.info("Entering request for update the category data with category id :{}",categoryId);
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        log.info("Completed request for update category with category id :{}",categoryId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);
    }

    /**
     * @apiNote this is related to get category by id
     * @since V 1.0
     * @author Prashant Bhagat
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
        log.info("Entering request for get category by id :{}",categoryId);
        CategoryDto categoryById = this.categoryService.getCategoryById(categoryId);
        log.info("Completed request for get category by id :{}",categoryId);
        return new ResponseEntity<>(categoryById,HttpStatus.OK);

    }

    /**
     * @apiNote this is related to get all categories
     * @author Prashant Bhagat
     * @since V 1.0
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = PaginationConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = PaginationConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = PaginationConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = PaginationConstants.SORT_DIR,required = false) String sortDir
    ){
        log.info("Entering request for get all categories by pagination and sorting");
        PageableResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed Request for get All categories By pagination And Sorting");
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    /**
     * @apiNote this is related to delete category
     * @author Prashant Bhagat
     * @since V 1.0
     * @param categoryId
     * @return
     */
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId){
        log.info("Entering Request for delete the category with category id:{}",categoryId);
        this.categoryService.deleteCategory(categoryId);
        log.info("Completed request for delete the category with category id :{}",categoryId);
        return new ResponseEntity<>(new ApiResponse(AppConstants.DELETE_RESPONSE,true),HttpStatus.OK);
    }
}
