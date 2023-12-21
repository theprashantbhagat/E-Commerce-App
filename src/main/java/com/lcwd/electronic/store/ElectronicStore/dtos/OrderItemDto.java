package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private Integer orderItemId;

    private Integer quantity;

    private Double totalPrice;

    private ProductDto product;

}
