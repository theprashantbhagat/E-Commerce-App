package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    private Category category;
    CategoryDto categoryDto1;
    CategoryDto categoryDto2;
    CategoryDto categoryDto3;

//    @Value("${jwt.token}")
//    private String jwtTkn;

    //String jwtTkn="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSb0BnbWFpbC5jb20iLCJleHAiOjE3MTc1ODc3ODYsImlhdCI6MTcxNzU2OTc4Nn0.8aOx0Gw64vp8WXcVBG3N41I88pm_NsM0-Ui0OlJCsexjhLExOetoqObREaUxrStuPF-6NtddcohIjQnu7xYInw";

    private String jwtTkn;

    @BeforeEach
    public void setUp() {
        jwtTkn = JwtTokenUtil.generateToken("Ro@gmail.com");
    }

    @BeforeEach
    public void init() {
        category = Category
                .builder()
                .title("Mobile phones")
                .description("this is related to phones")
                .coverImage("mob.png")
                .build();

         categoryDto1 = CategoryDto.builder().title("Mobile phones").description("this is related to phones").coverImage("mob.png").build();
         categoryDto2 = CategoryDto.builder().title("television").description("this is related to tvs").coverImage("tv.png").build();
         categoryDto3 = CategoryDto.builder().title("Earphones").description("this is related to earphones").coverImage("earp.png").build();

    }

    @Test
    public void createCategoryTest() throws Exception {
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/category/")
                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtTkn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    private String convertObjectToJsonString(Object user) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateCategoryTest() throws Exception {
        String categoryId = "cateAbc";
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.updateCategory(Mockito.any(), Mockito.anyString())).thenReturn(dto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/category/" + categoryId)
                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtTkn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getAllCategoryTest() throws Exception {

        PageableResponse<CategoryDto> pagResponse = new PageableResponse<>();
        pagResponse.setContent(Arrays.asList(categoryDto1, categoryDto2, categoryDto3));
        pagResponse.setLastPage(false);
        pagResponse.setPageNumber(100);
        pagResponse.setPageSize(10);
        pagResponse.setTotalElements(1000);

        Mockito.when(categoryService.getAllCategories(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagResponse);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/category/")
                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtTkn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getCategoryByIdTest() throws Exception {

        String categoryId="catAbcd";
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/category/" + categoryId)
                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void deleteCategoryTest() throws Exception {
        String categoryId="catAbcd";
        Mockito.doNothing().when(categoryService).deleteCategory(categoryId);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/category/" + categoryId)
                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void createProductWithCategoryTest(){
//
//        String categoryId="catAbcd";
//
//    }
}
