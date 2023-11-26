package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.BASE_URL)
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
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto)
    {
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * @apiNote this is related to update category
     * @author Prashant Bhagat
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId)
    {
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);
    }

    /**
     * @apiNote this is related to get category by id
     * @author Prashant Bhagat
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
        CategoryDto categoryById = this.categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryById,HttpStatus.OK);

    }

    /**
     * @apiNote this is related to get all categories
     * @author Prashant Bhagat
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
        PageableResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }
}
