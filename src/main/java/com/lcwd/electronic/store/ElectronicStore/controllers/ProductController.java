package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL)
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        ProductDto product = this.productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId){
        ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
        ProductDto productById = this.productService.getProductById(productId);
        return new ResponseEntity<>(productById,HttpStatus.OK);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId){
        this.productService.deleteProduct(productId);
        ApiResponse response = ApiResponse.builder().message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/product")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNum",defaultValue = PaginationConstants.PAGE_NUMBER,required = false) Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = PaginationConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = PaginationConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = PaginationConstants.SORT_DIR,required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNum, pageSize, sortBy, sortDir);
        return new ResponseEntity<PageableResponse<ProductDto>>(allProducts,HttpStatus.OK);
    }

    @GetMapping("product/search/{subTitle}")
    public ResponseEntity<List<ProductDto>> searchByTitle(String subTitle){

        List<ProductDto> productDtos = this.productService.searchByTitle(subTitle);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }
}
