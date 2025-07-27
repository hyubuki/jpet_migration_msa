package org.mybatis.jpetstore.cart.service;

import lombok.RequiredArgsConstructor;
import org.mybatis.jpetstore.common.domain.Cart;
import org.mybatis.jpetstore.common.domain.CartItem;
import org.mybatis.jpetstore.common.domain.Item;
import org.mybatis.jpetstore.common.grpc.CatalogGrpcClient;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CatalogGrpcClient catalogGrpcClient;

    /**
     * 장바구니에 아이템을 추가합니다.
     *
     * @param cart 장바구니
     * @param workingItemId 아이템 ID
     */
    public void addItem(final Cart cart, final String workingItemId){

        if (cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
        } else {
            boolean isInStock = catalogGrpcClient.isItemInStock(workingItemId);
            Item item = catalogGrpcClient.getItem(workingItemId);
            assert item != null;
            cart.addItem(item,isInStock);
        }
    }

    /**
     * 장바구니에서 아이템을 제거합니다.
     *
     * @param cart 장바구니
     * @param itemId 아이템 ID
     * @return 제거된 아이템
     */
    public Item removeItem(final Cart cart, final String itemId){
        return cart.removeItemById(itemId);
    }

    /**
     * 장바구니 수량을 갱신합니다.
     *
     * @param cart 장바구니
     * @param param 요청 파라미터
     */
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
