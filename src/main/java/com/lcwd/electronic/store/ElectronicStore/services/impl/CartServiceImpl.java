package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Cart;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        Integer quantity = request.getQuantity();
        String productId = request.getProductId();
        if(quantity<=0)
        {
            throw new BadApiRequestException("Requested quantity is not valid");
        }
        //fetch product
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " Id " + productId));
        //fetch user
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + userId));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //perform cart operation
        //if cart item already available then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map((item) -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true); //atomic reference
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        //create item
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice((quantity * product.getPrice()))
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return this.modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, Integer cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND + " id " + cartItemId));
        cartItemRepository.delete(cartItem);

    }

    @Override
    public void clearCart(String userId) {

    }
}
