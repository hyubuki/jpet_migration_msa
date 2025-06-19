package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.http.HttpFacade;
import org.mybatis.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class AccountController {
    private static final String REDIRECT_BASE_URL="http://localhost:8080";

    @Autowired
    private AccountService accountService;
    @Autowired
    private HttpFacade httpFacade;


    @GetMapping("/newAccountForm")
    public String newAccountForm(HttpSession session) {
        if (session.getAttribute("account") != null) {
            // 로그인이 되어 있으면 메인 페이지로 리다이렉트
            return "redirect:" + REDIRECT_BASE_URL + "/catalog";
        }
        return "account/NewAccountForm";
    }

    @PostMapping("/newAccount")
    public String newAccount(Account account, HttpSession session) {
        if (session.getAttribute("account") != null) {
            // 로그인이 되어 있으면 메인 페이지로 리다이렉트
            return "redirect:" + REDIRECT_BASE_URL + "/catalog";
        }
        accountService.insertAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));
        session.setAttribute("csrf_token", UUID.randomUUID().toString());

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
    public String editAccount(Account account, @RequestParam String csrf, HttpSession session, HttpServletRequest req) {
        if (csrf == null || session.getAttribute("account") == null || !csrf.equals(session.getAttribute("csrf_token"))) {
            // csrf가 null이거나 로그인이 안되어있거나 csrf가 일치하지 않으면 다시 돌아감
            String value = "This is not a valid request";
            req.setAttribute("msg", value);
            return "account/EditAccountForm";
        }
        accountService.updateAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        session.setAttribute("myList", httpFacade.getProductListByCategory(account.getFavouriteCategoryId()));
        return "redirect:" + REDIRECT_BASE_URL + "/catalog";
    }

    @GetMapping("/signonForm")
    public String signonForm(@RequestParam(required = false) String msg, HttpServletRequest req, HttpSession session) {
        if (session.getAttribute("account") != null) {
            // 로그인이 되어 있으면, 로그인 불가
            return "redirect:" + REDIRECT_BASE_URL + "/catalog";
        }
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
            session.setAttribute("csrf_token", UUID.randomUUID().toString());
            session.setAttribute("account", existAccount);
            session.setAttribute("myList", httpFacade.getProductListByCategory(existAccount.getFavouriteCategoryId()));
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
