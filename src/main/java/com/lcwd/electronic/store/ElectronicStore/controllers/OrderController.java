package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL+UrlConstants.ORDER_BASE)
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @apiNote this Api is for create order
     * @author Prashant Bhagat
     * @since V1.0
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request)
    {
        log.info("Entering request for create order");
        OrderDto order = orderService.createOrder(request);
        log.info("Completed request for create order");
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * @apiNote This Api is for remove order
     * @author Prashant Bhagat
     * @since V1.0
     * @param orderId
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId)
    {
        log.info("Entering request for remove order with order id :{}",orderId);
        orderService.removeOrder(orderId);
        ApiResponse response = ApiResponse.builder()
                .message(AppConstants.DELETE_CART_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for remove order with order id :{}",orderId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for get orders of user
     * @author Prashant Bhagat
     * @since V1.0
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        log.info("Entering request for get orders of user with user id :{}",userId);
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        log.info("Completed request for get orders of user with user id :{}",userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for get orders
     * @author Prashant Bhagat
     * @since V1.0
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.ORDER_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.PAGE_SIZE, required = false) String sortDir)
    {
        log.info("Entering request for get orders with pagination");
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for get orders with pagination");
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

}
