package org.mybatis.jpetstore.controller;


import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    protected static final String ERROR = "/WEB-INF/jsp/common/Error.jsp";
    private static final String NEW_ACCOUNT = "/WEB-INF/jsp/account/NewAccountForm.jsp";
    private static final String EDIT_ACCOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";
    private static final String SIGNON = "/WEB-INF/jsp/account/SignonForm.jsp";

    private static final List<String> LANGUAGE_LIST;
    private static final List<String> CATEGORY_LIST;

    @Autowired
    private AccountService accountService;

    static {
        LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
        CATEGORY_LIST = Collections.unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));
    }

    @GetMapping("/newAccountForm")
    public String newAccountForm() {
        return "account/NewAccountForm";
    }

    @PostMapping("/newAccount")
    public String newAccount(@RequestBody Account account, HttpSession session) {
        accountService.insertAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        // session.setAttribute("myList", catalogService.getProductListByCategory(account.getFavouriteCategoryId()));
        session.setAttribute("isAuthenticated", true);
        return "redirect:/";
    }

    @GetMapping("/editAccountForm")
    public String editAccountForm() {
        return "account/EditAccountForm";
    }

    @PostMapping("/editAccount")
    public String editAccount(@RequestBody Account account, HttpSession session) {
        accountService.updateAccount(account);
        session.setAttribute("account", accountService.getAccount(account.getUsername()));

        // 카탈로그 서비스 사용
        // session.setAttribute("myList", catalogService.getProductListByCategory(account.getFavouriteCategoryId()));
        return "redirect:/";
    }

    @GetMapping("/signonForm")
    public String signonForm() {
        return "account/SignonForm";
    }

    @PostMapping("/signon")
    public String signon(Account account, HttpSession session) {
        System.out.println(account.getUsername());
        account = accountService.getAccount(account.getUsername(), account.getPassword());

        if (account == null) {
            String value = "Invalid username or password.  Signon failed.";
            session.setAttribute("msg", value);
            session.invalidate();
            return "account/SignonForm";
        } else {
            account.setPassword(null);
//            session.setAttribute("myList", catalogService.getProductListByCategory(account.getFavouriteCategoryId()));
            session.setAttribute("isAuthenticated", true);
            return "redirect:/";
        }
    }

    @GetMapping("/signoff")
    public String signoff(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
