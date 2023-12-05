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
}
