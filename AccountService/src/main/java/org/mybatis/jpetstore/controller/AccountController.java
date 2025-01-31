package org.mybatis.jpetstore.controller;


import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.http.HttpFacade;
import org.mybatis.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class AccountController {
    private static final String REDIRECT_BASE_URL="http://localhost:8080";

    @Autowired
    private AccountService accountService;
    @Autowired
    private HttpFacade httpFacade;


    @GetMapping("/newAccountForm")
    public String newAccountForm() {
        return "account/NewAccountForm";
    }

    @PostMapping("/newAccount")
    public String newAccount(Account account, HttpSession session) {
        accountService.insertAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        session.setAttribute("myList", httpFacade.getProductListByCategory(account.getFavouriteCategoryId()));
        session.setAttribute("isAuthenticated", true);
        return "redirect:" + REDIRECT_BASE_URL + "/catalog";
    }

    @GetMapping("/editAccountForm")
    public String editAccountForm() {
        return "account/EditAccountForm";
    }

    @PostMapping("/editAccount")
    public String editAccount(Account account, HttpSession session) {
        accountService.updateAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        session.setAttribute("myList", httpFacade.getProductListByCategory(account.getFavouriteCategoryId()));
        return "redirect:" + REDIRECT_BASE_URL + "/catalog";
    }

    @GetMapping("/signonForm")
    public String signonForm(@RequestParam String msg, HttpServletRequest req) {
        if (msg != null)
            req.setAttribute("msg", msg);
        return "account/SignonForm";
    }

    @PostMapping("/signon")
    public String signon(Account account, HttpServletRequest req, HttpSession session) {
        Account existAccount = accountService.getAccount(account.getUsername(), account.getPassword());

        if (existAccount == null) {
            String value = "Invalid username or password.  Signon failed.";
            req.setAttribute("msg", value);
            session.invalidate();
            return "account/SignonForm";
        } else {
            account.setPassword(null);
            session.setAttribute("account", existAccount);
            session.setAttribute("myList", httpFacade.getProductListByCategory(account.getFavouriteCategoryId()));
            session.setAttribute("isAuthenticated", true);
            return "redirect:" + REDIRECT_BASE_URL + "/catalog";
        }
    }

    @GetMapping("/signoff")
    public String signoff(HttpSession session) {
        session.invalidate();
        return "redirect:" + REDIRECT_BASE_URL + "/catalog";
    }
}
