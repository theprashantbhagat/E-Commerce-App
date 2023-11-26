package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableHelper;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        log.info("Initiating dao call for create category data");
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category saveCat = this.categoryRepository.save(category);
        log.info("Completed Dao call for create category data");
        return this.modelMapper.map(saveCat, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

        log.info("Initiating dao call for update category data with category id :{}",categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category saveCat = this.categoryRepository.save(category);
        log.info("completed dao call for update category data with category id:{}",categoryId);
        return this.modelMapper.map(saveCat, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        log.info("Initiating dao call for get category by id :{}",categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + categoryId));
        log.info("Completed dao call for get category by id :{}",categoryId);
        return this.modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(Integer pageNum, Integer pageSize,String sortBy,String sortDir) {
        log.info("Initiating Dao call for get all categories by pagination and sorting");
        Sort sort = sortDir.equalsIgnoreCase("dsc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Category> page = this.categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response = PageableHelper.getPageableResponse(page, CategoryDto.class);
        log.info("Completed dao call for get all categories by pagination and sorting");
        return response;
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("Initiating dao call for delete the category with category id:{}",categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + categoryId));
        log.info("Completed dao call for delete the category with category id:{}",categoryId);
        this.categoryRepository.delete(category);
    }
}
