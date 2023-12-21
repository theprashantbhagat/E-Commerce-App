package com.lcwd.electronic.store.ElectronicStore.dtos;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT-PAID";
    private Double orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;
    private List<OrderItemDto> items = new ArrayList<>();
}
