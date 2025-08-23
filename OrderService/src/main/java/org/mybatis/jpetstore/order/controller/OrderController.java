package org.mybatis.jpetstore.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mybatis.jpetstore.common.domain.Account;
import org.mybatis.jpetstore.common.domain.Cart;
import org.mybatis.jpetstore.common.domain.Order;
import org.mybatis.jpetstore.order.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/listOrders")
    public String listOrders(HttpSession session, HttpServletRequest request, RedirectAttributes redirect, HttpServletResponse response) throws IOException {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            response.sendRedirect("/account/signonForm");
            return null;
        }
        List<Order> orderList = orderService.getOrdersByUsername(account.getUsername());
        request.setAttribute("orderList", orderList);
        return "order/ListOrders";
    }

    @GetMapping("/newOrderForm")
    public String newOrderForm(HttpServletRequest req, HttpSession session, RedirectAttributes redirect, HttpServletResponse response) throws IOException {
        Account account = (Account) session.getAttribute("account");
        Cart cart = (Cart) session.getAttribute("cart");
        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            response.sendRedirect("/account/signonForm");
            return null;
        }
        else if (cart != null) {
            Order order = orderService.createOrder(account, cart);
            session.setAttribute("order", order);
            return "order/NewOrderForm";
        }
        else {
            String msg = "An order could not be created because a cart could not be found.";
            req.setAttribute("msg", msg);
            return "common/Error";
        }
    }

    @PostMapping("/newOrder")
    public String newOrder(Order order, @RequestParam(required = false) boolean shippingAddressRequired, @RequestParam(required = false) boolean confirmed, @RequestParam(required = false) boolean changeShipInfo, @RequestParam String csrf, HttpServletRequest req, HttpSession session) {

        if (csrf == null || !csrf.equals(session.getAttribute("csrf_token"))) {
            String msg = "This is not a valid request";
            req.setAttribute("msg", msg);
            return "common/Error";
        }

        Order sessionOrder = (Order) session.getAttribute("order");

        var result = orderService.handleOrderProcess(sessionOrder, order, shippingAddressRequired,
                confirmed, changeShipInfo, session);
        if (result.getMessage() != null) {
            req.setAttribute("msg", result.getMessage());
        }
        return result.getViewName();
    }


    @GetMapping("/viewOrder")
    public String viewOrder(@RequestParam int orderId, HttpServletRequest req, HttpSession session, RedirectAttributes redirect, HttpServletResponse response) throws IOException {
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            response.sendRedirect("/account/signonForm");
            return null;
        }
        Order order = orderService.getOrder(orderId);
        if (account.getUsername().equals(order.getUsername())) {
            req.setAttribute("order", order);
            return "order/ViewOrder";
        } else {
            String msg = "You may only view your own orders.";
            req.setAttribute("msg", msg);
            return "common/Error";
        }
    }

}
