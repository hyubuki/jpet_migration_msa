package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.HttpRequest.HttpGetRequest;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.CartItem;
import org.mybatis.jpetstore.domain.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Controller
@RequestMapping("/")
public class CartController {

    @GetMapping("/addItemToCart")
    public String addItemToCart(@RequestParam String workingItemId, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            boolean isInStock = Boolean.TRUE.equals(HttpGetRequest.isItemInStockFromCatalogService(workingItemId));
            Item item = HttpGetRequest.getItemFromCatalogService(workingItemId);
            assert item != null;
            cart.addItem(item,isInStock);
        }
        model.addAttribute("cart",cart);
        return "cart/Cart";
    }


    @GetMapping("/viewCart")
    public String viewCart(Model model, HttpSession session) {
        Cart cart = getCart(session);
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
        Cart cart = getCart(session);
        Item item = cart.removeItemById(itemId);
        if (item == null) {
            model.addAttribute("msg", "Attempted to remove null CartItem from Cart.");
            return "common/error";
        } else {
            model.addAttribute("cart", cart);
            return "cart/Cart";
        }
    }

    @PostMapping("/update")
    public String updateCartQuantities(HttpServletRequest request, HttpSession session, Model model) {
        Cart cart = getCart(session);
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            try {
                int quantity = Integer.parseInt(request.getParameter(itemId));
                cart.setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                }
            } catch (Exception e) {
                // ignore parse exceptions on purpose
            }
        }
        model.addAttribute("cart",cart);
        return "cart/Cart";
    }

    @GetMapping("/checkout")
    public String checkOut(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart",cart);
        return "cart/Checkout";
    }



}
