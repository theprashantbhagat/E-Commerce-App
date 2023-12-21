package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "cart id is required")
    private String cartId;
    @NotBlank(message = "user id is required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "address id is required")
    private String billingAddress;
    @NotBlank(message = "phone id is required")
    private String billingPhone;
    @NotBlank(message = "name id is required")
    private String billingName;

}
