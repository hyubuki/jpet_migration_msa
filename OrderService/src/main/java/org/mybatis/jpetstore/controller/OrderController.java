package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class OrderController {
    private static final String REDIRECT_BASE_URL="http://localhost:8080";

    @Autowired
    OrderService orderService;

    @GetMapping("/listOrders")
    public String listOrders(HttpSession session, HttpServletRequest request, RedirectAttributes redirect) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            return "redirect:" + REDIRECT_BASE_URL + "/account/signonForm";
        }
        List<Order> orderList = orderService.getOrdersByUsername(account.getUsername());
        request.setAttribute("orderList", orderList);
        return "order/ListOrders";
    }

    @GetMapping("/newOrderForm")
    public String newOrderForm(HttpServletRequest req, HttpSession session, RedirectAttributes redirect) {
        Account account = (Account) session.getAttribute("account");
        Cart cart = (Cart) session.getAttribute("cart");
        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            return "redirect:" + REDIRECT_BASE_URL + "/account/signonForm";
        }
        else if (cart != null) {
            Order order = new Order();
            order.initOrder(account, cart);
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
        if (shippingAddressRequired) {
            changeBillInfo(sessionOrder, order);
            session.setAttribute("order", sessionOrder);
            return "order/ShippingForm";
        } else if(!confirmed) {
            if (changeShipInfo)
                changeShipInfo(sessionOrder, order);
            session.setAttribute("order", sessionOrder);
            return "order/ConfirmOrder";
        } else if (order != null) {
            try{
                orderService.insertOrder(sessionOrder);
                session.removeAttribute("cart");

                String msg = "Thank you, your order has been submitted.";
                req.setAttribute("msg", msg);

            }catch(Exception e){ // order 실패시 Error page로 이동

                String msg = "Fail to order";
                req.setAttribute("msg",msg);

                return "common/Error";
            }

            return "order/ViewOrder";
        } else {
            String msg = "An error occurred processing your order (order was null).";
            req.setAttribute("msg", msg);
            return "common/Error";
        }
    }

    @GetMapping("/viewOrder")
    public String viewOrder(@RequestParam int orderId, HttpServletRequest req, HttpSession session, RedirectAttributes redirect) {
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            String msg = "You must sign on before attempting to check out.  Please sign on and try checking out again.";
            redirect.addAttribute("msg", msg);
            return "redirect:" + REDIRECT_BASE_URL + "/account/signonForm";
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

    public void changeBillInfo(Order sessionOrder, Order order) {
        sessionOrder.setCardType(order.getCardType());
        sessionOrder.setCreditCard(order.getCreditCard());
        sessionOrder.setExpiryDate(order.getExpiryDate());
        sessionOrder.setBillToFirstName(order.getBillToFirstName());
        sessionOrder.setBillToLastName(order.getBillToLastName());
        sessionOrder.setBillAddress1(order.getBillAddress1());
        sessionOrder.setBillAddress2(order.getBillAddress2());
        sessionOrder.setBillCity(order.getBillCity());
        sessionOrder.setBillState(order.getBillState());
        sessionOrder.setBillZip(order.getBillZip());
        sessionOrder.setBillCountry(order.getBillCountry());
    }

    public void changeShipInfo(Order sessionOrder, Order order) {
        sessionOrder.setShipToFirstName(order.getShipToFirstName());
        sessionOrder.setShipToLastName(order.getShipToLastName());
        sessionOrder.setShipAddress1(order.getShipAddress1());
        sessionOrder.setShipAddress2(order.getShipAddress2());
        sessionOrder.setShipCity(order.getShipCity());
        sessionOrder.setShipState(order.getShipState());
        sessionOrder.setShipZip(order.getShipZip());
        sessionOrder.setShipCountry(order.getShipCountry());
    }
}
