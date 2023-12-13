package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
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
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;

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
    public void createProductTest(){

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto1 = productService.createProduct(modelMapper.map(product, ProductDto.class));
        Assertions.assertNotNull(productDto1);
        Assertions.assertEquals("MyPhone",productDto1.getTitle());
    }

    @Test
    public void updateProductTest(){

        String productId="prodAbc";
      ProductDto  productDto=ProductDto
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

      Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
      Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        Assertions.assertNotNull(productDto);

    }
    @Test
    public void getAllProducts(){
      Product product1=Product
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        Product product2=Product
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(products);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(1, 2, "title", "asc");
        Assertions.assertEquals(3,allProducts.getContent().size());
    }

    @Test
    public void getProductById(){

        String productId="prodAbcd";
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductById(productId);
        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getTitle(),productDto.getTitle(),"title not matched");
    }
    @Test
    public void deleteProduct(){

        String productId="prodAbc";
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);

    }
    @Test
    public void getAllLiveTest(){

        Product product1=Product
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        Product product2=Product
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();
        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(products);
        Mockito.when(productRepository.findByLiveTrue((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allLive = productService.getAllLive(1, 2, "title", "asc");
       // Assertions.assertEquals(true,product.getLive());
    }

    @Test
    public void searchTitleTest(){

        String keyword="MyPhone";
        Product product1=Product
                .builder().title("MyPhone 15")
                .description("this phone comes  with  c type charging slot").price(90000.00)
                .discountedPrice(800000.00).quantity(20)
                .live(true).stock(true)
                .imageName("15.png").build();
        Product product2=Product
                .builder().title("MyPhone 14")
                .description("this phone launche in 2022").price(80000.00)
                .discountedPrice(70000.00).quantity(30)
                .live(true).stock(true)
                .imageName("14.png").build();

        List<Product> products = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(products);
        Mockito.when(productRepository.findByTitleContaining(Mockito.anyString(),(Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> response = productService.searchByTitle(keyword, 1, 2, "title", "asc");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3,response.getContent().size());
    }

}
