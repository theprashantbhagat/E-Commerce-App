package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableHelper;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.profile.image.path}")
    private String path;
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        log.info("Initiating dao call for create product");
        Product product = this.modelMapper.map(productDto, Product.class);
        product.setAddedDate(new Date());
        Product save = this.productRepository.save(product);
        log.info("Completed dao call for create product");
        return this.modelMapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + productId));
        log.info("Initiating dao call for update product with product id:{}",productId);
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setStock(productDto.getStock());
        product.setLive(productDto.getLive());
        product.setAddedDate(new Date());
        product.setQuantity(productDto.getQuantity());
        product.setImageName(productDto.getImageName());
        Product updatedProduct = this.productRepository.save(product);
        log.info("Completed dao call for update product with product id:{}",productId);
        return this.modelMapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {

        log.info("Initiating dao call for delete product with product id:{}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + productId));
        String imageName = product.getImageName();
        String fullPath=path+imageName;
        File file=new File(fullPath);
        if(file.exists()){
            file.delete();
        }
        this.productRepository.delete(product);
        log.info("Completed dao call for delete product with product id:{}",productId);

    }

    @Override
    public ProductDto getProductById(String productId) {
        log.info("Initiating dao call for get product with product id:{}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + productId));
        log.info("Completed dao call for get product with product id:{}",productId);
        return this.modelMapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("dsc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        log.info("Initiating dao call for get all products with pagination");
        Page<Product> page = this.productRepository.findAll(pageable);
        PageableResponse<ProductDto> response = PageableHelper.getPageableResponse(page, ProductDto.class);
        log.info("Completed dao call for get all products with pagination");
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("dsc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        log.info("Initiating dao call for get all live products with pagination");
        Page<Product> page = this.productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> response = PageableHelper.getPageableResponse(page, ProductDto.class);
        log.info("Completed dao call for get all live products with pagination");
        return response;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,Integer pageNum, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("dsc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        log.info("Initiating dao call for search the product by title with pagination");
        Page<Product> page = this.productRepository.findByTitleContaining(subTitle,pageable);
        PageableResponse<ProductDto> response = PageableHelper.getPageableResponse(page, ProductDto.class);
        log.info("Completed dao call for search the product by title with pagination");
        return response;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + categoryId));
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        Product product = this.modelMapper.map(productDto, Product.class);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product save = this.productRepository.save(product);
        return this.modelMapper.map(save,ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + categoryId));
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + productId));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return this.modelMapper.map(savedProduct,ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,Integer pageNum, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("dsc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + categoryId));
        Page<Product> byCategory = this.productRepository.findByCategory(category,pageable);
        PageableResponse<ProductDto> response = PageableHelper.getPageableResponse(byCategory, ProductDto.class);
        return response;

    }
}
