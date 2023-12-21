package com.lcwd.electronic.store.ElectronicStore.controllers;
import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.BASE_URL+UrlConstants.CART_BASE)
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request)
    {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFrmCart(@PathVariable String userId,@PathVariable Integer cartItemId)
    {
        cartService.removeItemFromCart(userId,cartItemId);
        ApiResponse response = ApiResponse.builder()
                .message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId){

        cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder()
                .message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId)
    {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }


}
