package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class CartController {

    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/addItemToCart")
    public String addItemToCart(@RequestParam String workingItemId, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null){
            cart = new Cart();
        }

        cartService.addItem(cart,workingItemId);

        model.addAttribute("cart",cart);
        session.setAttribute("cart",cart);
        return "cart/Cart";
    }


    @GetMapping("/viewCart")
    public String viewCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null){
            cart = new Cart();
        }

        model.addAttribute("cart", cart);
        return "cart/Cart";
    }

    @GetMapping("/getCart")
    public Cart getCart(HttpSession session) {
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new Cart());
        }
        return (Cart) session.getAttribute("cart");
    }

    @GetMapping("/remove/item")
    public String removeItemFromCart(@RequestParam String itemId, HttpSession session, Model model) {

        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null){
            cart = new Cart();
        }

        Item item = cartService.removeItem(cart,itemId);

        if (item == null) {
            model.addAttribute("msg", "Attempted to remove null CartItem from Cart.");
            return "common/error";
        } else {
            model.addAttribute("cart", cart);
            session.setAttribute("cart",cart);
            return "cart/Cart";
        }
    }

    @PostMapping("/update")
    public String updateCartQuantities(HttpServletRequest request, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null){
            cart = new Cart();
        }

        cartService.updateCartItem(cart,request.getParameterMap());
        model.addAttribute("cart",cart);
        session.setAttribute("cart",cart);
        return "cart/Cart";
    }


}
