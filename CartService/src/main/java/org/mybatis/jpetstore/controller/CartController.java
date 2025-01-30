package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private final Cart cart  = new Cart();

    @PostMapping("/addItemToCart")
    public void addItemToCart(@RequestParam String workingItemId){
        if(cart.containsItemId(workingItemId)){
            cart.incrementQuantityByItemId(workingItemId);
        }else{
            // isInStock is a "real-time" property that must be updated
            // every time an item is added to the cart, even if other
            // item details are cached.
            boolean isInstock =
        }
    }



}
