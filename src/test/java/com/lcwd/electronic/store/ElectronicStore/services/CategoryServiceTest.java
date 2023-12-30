package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;
    private Category category;

    @BeforeEach
    public void init(){

        category=Category
                .builder()
                .title("Mobile phones")
                .description("this is related to phones")
                .coverImage("mob.png")
                .build();
    }

    @Test
    public void createCategoryTest(){

        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto category1 = categoryService.createCategory(modelMapper.map(category, CategoryDto.class));
        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Mobile phones",category1.getTitle());

    }

    @Test
    public void updateCategoryTest(){

        String categoryId="";

       CategoryDto categoryDto=CategoryDto
                .builder()
                .title("Mobile phones")
                .description("this is related to phones")
                .coverImage("mob.png")
                .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        Assertions.assertNotNull(categoryDto);
    }

    @Test
    public void getAllCategoriesTest(){

       Category category1=Category
                .builder()
                .title("Mobile phones")
                .description("this is related to phones")
                .coverImage("mob.png")
                .build();
       Category category2=Category
                .builder()
                .title("Mobile phones")
                .description("this is related to phones")
                .coverImage("mob.png")
                .build();

        List<Category> categories = Arrays.asList(category, category1, category2);
        Page<Category> page=new PageImpl<>(categories);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(1, 2, "title", "asc");
        Assertions.assertEquals(3,allCategories.getContent().size());
    }

    @Test
    public void getCategoryById(){

        String categoryId="userIdabcd";
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle(),"title not matched");

    }

    @Test
    public void deleteCategoryTest(){

        String categoryId="userabcd";
        Mockito.when(categoryRepository.findById("userabcd")).thenReturn(Optional.of(category));
        categoryService.deleteCategory(categoryId);

    }
    @Test
    public void createWithCategoryTest(){

        String categoryId="catAbc";


    }




}
