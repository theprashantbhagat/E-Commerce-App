package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Integer cartItemId;

    private Integer quantity;

    private Integer totalPrice;


    private ProductDto product;

}
