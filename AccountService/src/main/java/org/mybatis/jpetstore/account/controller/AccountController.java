package org.mybatis.jpetstore.account.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mybatis.jpetstore.common.domain.Account;
import org.mybatis.jpetstore.common.grpc.CatalogGrpcClient;
import org.mybatis.jpetstore.account.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final CatalogGrpcClient catalogGrpcClient;


    @GetMapping("/newAccountForm")
    public String newAccountForm(HttpSession session, HttpServletResponse resp) throws IOException {
        if (session.getAttribute("account") != null) {
            // 로그인이 되어 있으면 메인 페이지로 리다이렉트
            resp.sendRedirect("/catalog");
        }
        return "account/NewAccountForm";
    }

    @PostMapping("/newAccount")
    public String newAccount(Account account, HttpSession session, HttpServletResponse resp) throws IOException {
        if (session.getAttribute("account") == null) {
            // 로그인이 되어 있지 않으면
            accountService.insertAccount(account);
            session.setAttribute("account", accountService.getAccount(account.getUsername()));
            session.setAttribute("csrf_token", UUID.randomUUID().toString());

            // 카탈로그 서비스 사용
            session.setAttribute("myList", catalogGrpcClient.getProductListByCategory(account.getFavouriteCategoryId()));
            session.setAttribute("isAuthenticated", true);
        }
        resp.sendRedirect("/catalog");
        return null;
    }

    @GetMapping("/editAccountForm")
    public String editAccountForm() {
        return "account/EditAccountForm";
    }

    @PostMapping("/editAccount")
    public String editAccount(Account account, @RequestParam String csrf, HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (csrf == null || session.getAttribute("account") == null || !csrf.equals(session.getAttribute("csrf_token"))) {
            // csrf가 null이거나 로그인이 안되어있거나 csrf가 일치하지 않으면 다시 돌아감
            String value = "This is not a valid request";
            req.setAttribute("msg", value);
            return "account/EditAccountForm";
        }
        accountService.updateAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        session.setAttribute("myList", catalogGrpcClient.getProductListByCategory(account.getFavouriteCategoryId()));
        resp.sendRedirect("/catalog");
        return null;
    }

    @GetMapping("/signonForm")
    public String signonForm(@RequestParam(required = false) String msg, HttpServletRequest req, HttpSession session, HttpServletResponse resp) throws IOException {
        if (session.getAttribute("account") != null) {
            // 로그인이 되어 있으면, 로그인 불가
            resp.sendRedirect("/catalog");
            return null;
        }
        if (msg != null)
            req.setAttribute("msg", msg);
        return "account/SignonForm";
    }

    @PostMapping("/signon")
    public String signon(Account account, HttpServletRequest req, HttpSession session, HttpServletResponse resp) throws IOException {
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
            session.setAttribute("myList", catalogGrpcClient.getProductListByCategory(existAccount.getFavouriteCategoryId()));
            session.setAttribute("isAuthenticated", true);
            resp.sendRedirect("/catalog");
            return null;
        }
    }

    @GetMapping("/signoff")
    public String signoff(HttpSession session, HttpServletResponse resp) throws IOException {
        session.invalidate();
        resp.sendRedirect("/catalog");
        return null;
    }
}
