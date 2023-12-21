package com.lcwd.electronic.store.ElectronicStore.services;
import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest orderDto);

    void removeOrder(String orderId);

    List<OrderDto> getOrdersOfUser(String userId);

    PageableResponse<OrderDto> getOrders(int pageNum,int pageSize,String sortBy,String sortDir);


}
