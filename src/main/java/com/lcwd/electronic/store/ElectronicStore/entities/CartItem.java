package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @Column(name = "cart_item_quantity")
    private Integer quantity;

    @Column(name = "cart_item_total_price")
    private Double totalPrice;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_Id")
    private Cart cart;
}
