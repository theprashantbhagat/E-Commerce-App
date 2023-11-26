package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    CategoryDto getCategoryById(String categoryId);

    PageableResponse<CategoryDto> getAllCategories(Integer pageNum, Integer pageSize, String sortBy, String sortDir);

    void deleteCategory(String categoryId);
}
