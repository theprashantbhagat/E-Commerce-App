package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @MockBean
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;
    private Product product;

    @BeforeEach
    public void init(){

        product=Product
                .builder()
                .title("MyPhone 15")
                .description("this phone comes  with  c type charging slot")
                .price(90000.00)
                .discountedPrice(800000.00)
                .quantity(20)
                .live(true)
                .stock(true)
                .imageName("15.png")
                .build();
    }
    @Test
    public void createProductTest() throws Exception {
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }

    private String convertObjectToJsonString(Object product) {

        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Test
    public void updateProductTest() throws Exception {

        String productId="prodabcd";
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.updateProduct(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/api/product/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }
    @Test
    public void getAllProductsTest() throws Exception {

        ProductDto product1=ProductDto
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        ProductDto product2=ProductDto
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();

        List<ProductDto> productDtos = Arrays.asList( product1, product2);
        PageableResponse<ProductDto> pResp=new PageableResponse<>();
        pResp.setContent(productDtos);
        pResp.setLastPage(false);
        pResp.setPageNumber(100);
        pResp.setPageSize(10);
        pResp.setTotalElements(1000);

        Mockito.when(productService.getAllProducts(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pResp);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void getProductByIdTest() throws Exception {

        String productId="prodabcd";
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.getProductById(Mockito.anyString())).thenReturn(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/product/"+productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void deleteProductTest() throws Exception {

        String productId="prodabcd";
        Mockito.doNothing().when(productService).deleteProduct(productId);
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/product/"+productId)
        ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void searchByTitleTest() throws Exception {
        String keyword="MyPhone";
        ProductDto product1=ProductDto
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        ProductDto product2=ProductDto
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();

        List<ProductDto> productDtos = Arrays.asList(product1, product2);
        PageableResponse<ProductDto> pResp=new PageableResponse<>();
        pResp.setContent(productDtos);
        pResp.setLastPage(false);
        pResp.setPageNumber(100);
        pResp.setPageSize(10);
        pResp.setTotalElements(1000);

        Mockito.when(productService.searchByTitle(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pResp);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/product/search/"+keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getAllLiveTest() throws Exception {

        ProductDto product1=ProductDto
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        ProductDto product2=ProductDto
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();

        List<ProductDto> productDtos = Arrays.asList(product1, product2);
        PageableResponse<ProductDto> pResp=new PageableResponse<>();
        pResp.setContent(productDtos);
        pResp.setLastPage(false);
        pResp.setPageNumber(100);
        pResp.setPageSize(10);
        pResp.setTotalElements(1000);

        Mockito.when(productService.getAllLive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pResp);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/product/live")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

}
