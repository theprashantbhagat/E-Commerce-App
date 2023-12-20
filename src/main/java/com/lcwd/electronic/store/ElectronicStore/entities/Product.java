package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;

    @Column(name = "product_title")
    private String title;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_price")
    private Double price;

    @Column(name = "product_discounted_price")
    private Double discountedPrice;

    @Column(name = "product_quantity")
    private Integer quantity;

    @Column(name = "product_added_date")
    private Date addedDate;

    @Column(name = "product_live")
    private Boolean live;

    @Column(name = "product_stock")
    private Boolean stock;

    @Column(name = "product_image_name")
    private String imageName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


}
