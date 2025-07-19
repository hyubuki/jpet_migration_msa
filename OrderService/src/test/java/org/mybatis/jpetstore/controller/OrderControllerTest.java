package org.mybatis.jpetstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mybatis.jpetstore.controller.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.mock.web.MockHttpSession;

import org.mybatis.jpetstore.service.OrderService;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.dto.OrderProcessResult;

import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@TestPropertySource(properties = "gateway.base-url=http://localhost")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    // 로그인된 사용자가 주문 목록을 조회하면 목록을 보여준다
    @Test
    void listOrdersWithLoginShowsList() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Account account = new Account();
        account.setUsername("user");
        session.setAttribute("account", account);
        when(orderService.getOrdersByUsername("user")).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/listOrders").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/ListOrders"));
    }

    // 로그인하지 않은 사용자가 주문 목록을 조회하면 로그인 페이지로 이동한다
    @Test
    void listOrdersWithoutLoginRedirectsToSignon() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listOrders"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account/signonForm"));
    }

    // 장바구니가 없는 상태에서 주문을 생성하면 에러 페이지를 반환한다
    @Test
    void newOrderWhenCartMissingShowsError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        mockMvc.perform(MockMvcRequestBuilders.get("/newOrderForm").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("common/Error"));
    }

    // 로그인과 장바구니가 있으면 주문 폼을 보여준다
    @Test
    void newOrderFormWithCartShowsForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        session.setAttribute("cart", new Cart());
        when(orderService.createOrder(org.mockito.ArgumentMatchers.any(Account.class), org.mockito.ArgumentMatchers.any(Cart.class)))
                .thenReturn(new Order());
        mockMvc.perform(MockMvcRequestBuilders.get("/newOrderForm").session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/NewOrderForm"));
    }

    // 로그인하지 않은 사용자가 주문 폼을 요청하면 로그인 페이지로 이동한다
    @Test
    void newOrderFormWithoutLoginRedirects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/newOrderForm"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account/signonForm"));
    }

    // 정상적으로 주문 처리가 완료되면 주문 상세 화면으로 이동한다
    @Test
    void newOrderProcessSuccessRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Order sessionOrder = new Order();
        session.setAttribute("order", sessionOrder);
        session.setAttribute("csrf_token", "token");
        when(orderService.handleOrderProcess(sessionOrder, sessionOrder, false, true, false, session))
                .thenReturn(new OrderProcessResult("order/ViewOrder", null));

        mockMvc.perform(MockMvcRequestBuilders.post("/newOrder")
                .session(session)
                .param("csrf", "token"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/ViewOrder"));
    }

    // 잘못된 CSRF 토큰이면 에러 페이지를 반환한다
    @Test
    void newOrderInvalidCsrfShowsError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("order", new Order());
        session.setAttribute("csrf_token", "token");
        mockMvc.perform(MockMvcRequestBuilders.post("/newOrder")
                .session(session)
                .param("csrf", "bad"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("common/Error"));
    }

    // 자신의 주문을 조회하면 주문 상세 뷰를 반환한다
    @Test
    void viewOrderAsOwnerShowsOrder() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Account account = new Account();
        account.setUsername("user");
        session.setAttribute("account", account);
        Order order = new Order();
        order.setUsername("user");
        when(orderService.getOrder(1)).thenReturn(order);
        mockMvc.perform(MockMvcRequestBuilders.get("/viewOrder").session(session).param("orderId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order/ViewOrder"));
    }

    // 로그인하지 않은 사용자가 주문 조회 시 로그인 페이지로 이동한다
    @Test
    void viewOrderWithoutLoginRedirects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/viewOrder").param("orderId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/account/signonForm"));
    }

    // 다른 사용자의 주문을 조회하면 에러 페이지를 반환한다
    @Test
    void viewOrderNotOwnerShowsError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Account account = new Account();
        account.setUsername("user");
        session.setAttribute("account", account);
        Order order = new Order();
        order.setUsername("other");
        when(orderService.getOrder(1)).thenReturn(order);
        mockMvc.perform(MockMvcRequestBuilders.get("/viewOrder").session(session).param("orderId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("common/Error"));
    }
}
