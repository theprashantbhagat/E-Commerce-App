package com.lcwd.electronic.store.ElectronicStore.controllers;
import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.CartDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.BASE_URL+UrlConstants.CART_BASE)
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @apiNote This Api is for add Item to cart
     * @author Prashant Bhagat
     * @since V1.0
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request)
    {
        log.info("Entering request for add item to cart with user id :{}",userId);
        CartDto cartDto = cartService.addItemToCart(userId, request);
        log.info("Completed request for add item to cart with user id :{}",userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    /**
     * @apiNote this Api is for remove item from cart
     * @author Prashant Bhagat
     * @since V1.0
     * @param userId
     * @param cartItemId
     * @return
     */
    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFrmCart(@PathVariable String userId,@PathVariable Integer cartItemId)
    {
        log.info("Entering request for remove item from cart with cart item id :{}",cartItemId);
        cartService.removeItemFromCart(userId,cartItemId);
        ApiResponse response = ApiResponse.builder()
                .message(AppConstants.DELETE_CART_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for remove item from cart with cart item id :{}",cartItemId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @apiNote this Api is for clear cart
     * @author Prashant Bhagat
     * @since V1.0
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId){

        log.info("Entering request for clear cart with user id :{}",userId);
        cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder()
                .message(AppConstants.DELETE_CART_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for clear cart with user id :{}",userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @apiNote This Api is for get cart by user
     * @author Prashant Bhagat
     * @since V1.0
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId)
    {
        log.info("Entering request for get cart with user id :{}",userId);
        CartDto cartDto = cartService.getCartByUser(userId);
        log.info("Completed request for get cart with user id :{}",userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }


}
