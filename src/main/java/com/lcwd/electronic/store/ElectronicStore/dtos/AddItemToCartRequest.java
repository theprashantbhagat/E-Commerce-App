package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddItemToCartRequest {

    private String productId;

    private Integer quantity;


}
