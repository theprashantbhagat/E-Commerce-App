package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(UrlConstants.BASE_URL + UrlConstants.CATEGORY_BASE)
@Slf4j
public class CategoryController {

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String path;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    /**
     * @param categoryDto
     * @return
     * @apiNote this is related to create category
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Entering request for create category data");
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        log.info("Completed request for create category data");
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * @param categoryDto
     * @param categoryId
     * @return
     * @apiNote this is related to update category
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        log.info("Entering request for update the category data with category id :{}", categoryId);
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        log.info("Completed request for update category with category id :{}", categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    /**
     * @param categoryId
     * @return
     * @apiNote this is related to get category by id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        log.info("Entering request for get category by id :{}", categoryId);
        CategoryDto categoryById = this.categoryService.getCategoryById(categoryId);
        log.info("Completed request for get category by id :{}", categoryId);
        return new ResponseEntity<>(categoryById, HttpStatus.OK);

    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote this is related to get all categories
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
    ) {
        log.info("Entering request for get all categories by pagination and sorting");
        PageableResponse<CategoryDto> allCategories = this.categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed Request for get All categories By pagination And Sorting");
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    /**
     * @param categoryId
     * @return
     * @apiNote this is related to delete category
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryId) {
        log.info("Entering Request for delete the category with category id:{}", categoryId);
        this.categoryService.deleteCategory(categoryId);
        ApiResponse response = ApiResponse.builder().message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for delete the category with category id :{}", categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     * @apiNote This method is related to upload cover image
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCoverImage(@RequestParam MultipartFile image, @PathVariable String categoryId) throws IOException {

        log.info("Entering request for upload cover image with category id :{}", categoryId);
        String uploadFile = fileService.uploadFile(image, path);
        CategoryDto cat = categoryService.getCategoryById(categoryId);
        cat.setCoverImage(uploadFile);
        categoryService.updateCategory(cat, categoryId);
        ImageResponse imageUploaded = ImageResponse.builder().imageName(uploadFile).message("Image Uploaded").success(true).status(HttpStatus.CREATED).build();
        log.info("Completed request for upload cover image with category id :{}", categoryId);
        return new ResponseEntity<>(imageUploaded, HttpStatus.CREATED);
    }

    /**
     * @param categoryId
     * @param response
     * @throws IOException
     * @apiNote This method is related to serve cover Image
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/image/{categoryId}")
    public void serveCoverImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        log.info("Entering Request for get cover image with category id :{}", categoryId);
        CategoryDto category = categoryService.getCategoryById(categoryId);
        InputStream resource = fileService.getResource(path, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed Request for get cover image with category id :{}", categoryId);
    }


    /**
     * @param productDto
     * @param categoryId
     * @return
     * @apiNote Api for create product with category id
     * @author Prashant Bhagat
     * @since V 1.0
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto, @PathVariable String categoryId) {

        log.info("Entering Request for create product with category id :{}", categoryId);
        ProductDto category = this.productService.createWithCategory(productDto, categoryId);
        log.info("Completed Request for create product with category id :{}", categoryId);
        return new ResponseEntity<>(category, HttpStatus.CREATED);

    }

    /**
     * @param productId
     * @param categoryId
     * @return
     * @apiNote Api for update category with product id and category id
     * @authoe Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String productId, @PathVariable String categoryId) {
        log.info("Entering Request for update category with product id and category id :{}", productId, categoryId);
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        log.info("Completed Request for update category with product id and category id :{}", productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * @param categoryId
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote Api for get all products with category id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNum", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.CATEGORY_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
    ) {

        log.info("Entering Request for get all products of with category id:{}", categoryId);
        PageableResponse<ProductDto> allOfCategory = this.productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed Request for get all products of with category id:{}", categoryId);
        return new ResponseEntity<>(allOfCategory, HttpStatus.OK);
    }


}
