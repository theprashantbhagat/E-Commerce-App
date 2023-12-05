package com.lcwd.electronic.store.ElectronicStore.repositories;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByTitleContaining(String subTitle,Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);
}
