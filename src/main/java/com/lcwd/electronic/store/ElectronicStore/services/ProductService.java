package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto ,String productId);

    void deleteProduct(String productId);

    ProductDto getProductById(String productId);

    PageableResponse<ProductDto> getAllProducts(Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> getAllLive(Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String subTitle,Integer pageNum, Integer pageSize, String sortBy, String sortDir);


    //Create product with category
    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    //update category of product
    ProductDto updateCategory(String productId,String categoryId);

    //get category containing all the products
    PageableResponse<ProductDto> getAllOfCategory(String categoryId,Integer pageNum, Integer pageSize, String sortBy, String sortDir);









}
