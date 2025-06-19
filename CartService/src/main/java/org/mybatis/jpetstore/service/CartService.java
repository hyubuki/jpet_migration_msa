package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.HttpRequest.HttpGetRequest;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.CartItem;
import org.mybatis.jpetstore.domain.Item;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
public class CartService {

    public void addItem(final Cart cart, final String workingItemId){

        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            boolean isInStock = Boolean.TRUE.equals(HttpGetRequest.isItemInStockFromCatalogService(workingItemId));
            Item item = HttpGetRequest.getItemFromCatalogService(workingItemId);
            assert item != null;
            cart.addItem(item,isInStock);
        }
    }

    public Item removeItem(final Cart cart, final String itemId){
        return cart.removeItemById(itemId);
    }

    public void updateCartItem(final Cart cart, final Map<String, String[]> param){
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();

            try {
                String[] quantityInput = param.get(itemId);
                if (quantityInput == null ||  quantityInput.length == 0){
                    continue;
                }
                int quantity = Integer.parseInt(quantityInput[0]);
                if (quantity < 1) {
                    cartItems.remove();
                }
                cart.setQuantityByItemId(itemId, quantity);

            } catch (Exception e) {
                // ignore parse exceptions on purpose
            }
        }

    }



}
