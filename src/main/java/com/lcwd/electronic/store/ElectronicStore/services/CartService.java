package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;

public interface CartService {

    //add items in cart
    //case1:if cart for user is not available then we will create cart and add item
    //case2:if cart is available then add items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //Remove item from cart
    void removeItemFromCart(String userId, Integer cartItemId);

    //Remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);

}
