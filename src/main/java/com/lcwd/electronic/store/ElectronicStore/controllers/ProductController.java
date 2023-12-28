package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
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
@Slf4j
@RequestMapping(UrlConstants.BASE_URL + UrlConstants.PRODUCT_BASE)
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String path;

    /**
     * @param productDto
     * @return
     * @apiNote this Api is for create product
     * @author Prashant Bhagat
     * @since V 1.0
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Entering request for create product");
        ProductDto product = this.productService.createProduct(productDto);
        log.info("Completed request for create product");
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * @param productDto
     * @param productId
     * @return
     * @apiNote This Api is for update the product with product id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {
        log.info("Entering request for update product with product id:{}" + productId);
        ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
        log.info("Completed request for update product with product id:{}" + productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    /**
     * @param productId
     * @return
     * @apiNote This Api is for get the product with product id
     * @author Prashant Bhagat
     * @since V 1.0
     */

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        log.info("Entering request for get product with product id:{}" + productId);
        ProductDto productById = this.productService.getProductById(productId);
        log.info("Completed request for get product with product id:{}" + productId);
        return new ResponseEntity<>(productById, HttpStatus.OK);
    }

    /**
     * @param productId
     * @return
     * @apiNote This Api is for delete the product with product id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        log.info("Entering request for delete product with product id:{}" + productId);
        this.productService.deleteProduct(productId);
        ApiResponse response = ApiResponse.builder().message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for delete product with product id:{}" + productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote This Api is for get all products with pagination
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNum", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
    ) {
        log.info("Entering request for get all products with pagination");
        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNum, pageSize, sortBy, sortDir);
        log.info("Completed request for get all products with pagination");
        return new ResponseEntity<PageableResponse<ProductDto>>(allProducts, HttpStatus.OK);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote This Api is for get all live products with pagination
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNum", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
    ) {
        log.info("Entering request for get all live products with pagination");
        PageableResponse<ProductDto> allProducts = this.productService.getAllLive(pageNum, pageSize, sortBy, sortDir);
        log.info("Completed request for get all live products with pagination");
        return new ResponseEntity<PageableResponse<ProductDto>>(allProducts, HttpStatus.OK);
    }


    /**
     * @param subTitle
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote This Api is for search the product with pagination
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
            @PathVariable String subTitle,
            @RequestParam(value = "pageNum", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
    ) {
        log.info("Entering request for search the products by title with pagination");
        PageableResponse<ProductDto> allProducts = this.productService.searchByTitle(subTitle, pageNum, pageSize, sortBy, sortDir);
        log.info("Entering request for search the products by title with pagination");
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    /**
     * @param image
     * @param productId
     * @return
     * @throws IOException
     * @apiNote This Api is for upload the product image with product id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable MultipartFile image, String productId) throws IOException {
        log.info("Entering request for upload the product image with product id:{}", productId);
        String file = fileService.uploadFile(image, path);
        ProductDto product = productService.getProductById(productId);
        product.setImageName(file);
        productService.updateProduct(product, productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(file).message("product image uploaded").success(true).status(HttpStatus.OK).build();
        log.info("Completed request for upload the product image with product id:{}", productId);
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);

    }

    /**
     * @param productId
     * @param response
     * @throws IOException
     * @apiNote This Api is for serve the product image with product id
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/image/{productId}")
    public void getImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        log.info("Entering request for get the product image with product id:{}", productId);
        ProductDto product = productService.getProductById(productId);
        InputStream resource = fileService.getResource(path, product.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed request for get the product image with product id:{}", productId);
    }


}
