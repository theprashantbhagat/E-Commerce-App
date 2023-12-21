package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.entities.*;
import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableHelper;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.OrderRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String cartId = orderDto.getCartId();
        String userId = orderDto.getUserId();
        //fetch user and fetch cart
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + cartId));
        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequestException("Invalid number of items in cart");
        }
        Order order = Order.builder().billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user).build();

        //cartItem to orderItem
        AtomicReference<Double> orderAmount=new AtomicReference<>(0.0);
        List<OrderItem> items = cartItems.stream().map(cartItem -> {

          OrderItem orderItem=  OrderItem.builder().quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                    .order(order).build();

          orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());

            return  orderItem;
        }).collect(Collectors.toList());

        order.setItems(items);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return this.modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + orderId));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + "id" + userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(order -> this.modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return PageableHelper.getPageableResponse(page,OrderDto.class);
    }
}
