package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.HttpRequest.HttpGetRequest;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class CartController {

    private final Cart cart  = new Cart();

    @GetMapping("/addItemToCart")
    public void addItemToCart(@RequestParam String workingItemId){
        if(cart.containsItemId(workingItemId)){
            cart.incrementQuantityByItemId(workingItemId);
        }else{
            // isInStock is a "real-time" property that must be updated
            // every time an item is added to the cart, even if other
            // item details are cached.
//            boolean isInstock =
        }
    }

    @GetMapping("/test1")
    public Item test1(){
        System.out.println("test1");
        System.out.println(HttpGetRequest.getItemFromCatalogService("EST-1"));
        return HttpGetRequest.getItemFromCatalogService("EST-1");
    }

    @GetMapping("/test2")
    public void test2(){
        System.out.println("test2");
        HttpGetRequest.isItemInStockFromCatalogService("EST-1");
    }


}
