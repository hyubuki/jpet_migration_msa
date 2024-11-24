package org.mybatis.jpetstore.controller;


import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    static {
        LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
        CATEGORY_LIST = Collections.unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));
    }

    public Account getAccount() {
        return (Account) redisTemplate.opsForValue().get("account");
    }

    public String getUsername() {
        Account account = (Account) redisTemplate.opsForValue().get("account");
        if (account == null)
            return null;
        return account.getUsername();
    }

    public void setUsername(String username) {
        Account account = (Account) redisTemplate.opsForValue().get("account");
        account.setUsername(username);
        redisTemplate.opsForValue().set("account", account);
    }

    public String getPassword() {
        Account account = (Account) redisTemplate.opsForValue().get("account");
        if (account == null)
            return null;
        return account.getPassword();
    }

    public void setPassword(String password) {
        Account account = (Account) redisTemplate.opsForValue().get("account");
        account.setPassword(password);
        redisTemplate.opsForValue().set("account", account);
    }

    public List<Product> getMyList() {
        List<Product> myList = (List<Product>) redisTemplate.opsForValue().get("myList");
        if (myList == null)
            return null;
        return myList;
    }

    public void setMyList(List<Product> myList) {
        redisTemplate.opsForValue().set("myList", myList);
    }

    public List<String> getLanguages() {
        return LANGUAGE_LIST;
    }

    public List<String> getCategories() {
        return CATEGORY_LIST;
    }

    @GetMapping("/newAccountForm")
    public String newAccountForm() {
        System.out.println("newAccountForm");
        return "account/NewAccountForm";
    }
}
