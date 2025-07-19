package org.mybatis.jpetstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mybatis.jpetstore.controller.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.mock.web.MockHttpSession;

import org.mybatis.jpetstore.service.CartService;
import org.mybatis.jpetstore.domain.Cart;

import static org.mockito.Mockito.when;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    // 파라미터가 없으면 400 오류를 반환한다
    @Test
    void addItemToCartMissingParamReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addItemToCart"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 장바구니에 아이템을 추가하면 장바구니 페이지를 보여준다
    @Test
    void addItemToCartAddsItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addItemToCart").param("workingItemId", "I1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cart/Cart"));
    }

    // 장바구니 페이지를 요청하면 장바구니 뷰를 반환한다
    @Test
    void viewCartShowsCart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new Cart());
        mockMvc.perform(MockMvcRequestBuilders.get("/viewCart").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cart/Cart"));
    }

    // getCart 호출 시 세션에 장바구니를 생성해 반환한다
    @Test
    void getCartCreatesCartWhenMissing() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(MockMvcRequestBuilders.get("/getCart").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // 아이템을 성공적으로 제거하면 장바구니 뷰를 반환한다
    @Test
    void removeItemFromCartSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new Cart());
        when(cartService.removeItem(org.mockito.ArgumentMatchers.any(Cart.class), org.mockito.ArgumentMatchers.eq("I1")))
                .thenReturn(new org.mybatis.jpetstore.domain.Item());
        mockMvc.perform(MockMvcRequestBuilders.get("/remove/item").session(session).param("itemId", "I1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cart/Cart"));
    }

    // 수량 업데이트 후 장바구니 뷰를 반환한다
    @Test
    void updateCartQuantitiesUpdatesCart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new Cart());
        mockMvc.perform(MockMvcRequestBuilders.post("/update").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cart/Cart"));
    }

    // 존재하지 않는 아이템을 삭제하려고 하면 오류 페이지를 반환한다
    @Test
    void removeItemFromCartWhenMissingShowsError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new Cart());
        when(cartService.removeItem(org.mockito.ArgumentMatchers.any(Cart.class), org.mockito.ArgumentMatchers.eq("BAD"))).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/remove/item").session(session).param("itemId", "BAD"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("common/error"));
    }
}
