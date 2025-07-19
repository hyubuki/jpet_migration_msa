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

import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.http.HttpFacade;
import org.mybatis.jpetstore.domain.Account;

import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
@TestPropertySource(properties = "gateway.base-url=http://localhost")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private HttpFacade httpFacade;

    // 계정을 생성하면 메인 화면으로 리다이렉트된다
    @Test
    void newAccountCreatesAccountAndRedirects() throws Exception {
        when(accountService.getAccount("user")).thenReturn(new Account());
        when(httpFacade.getProductListByCategory(org.mockito.ArgumentMatchers.any()))
                .thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/newAccount")
                .param("username", "user")
                .param("password", "pass")
                .param("favouriteCategoryId", "CAT"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 수정 폼 요청 시 정상적으로 뷰를 반환한다
    @Test
    void editAccountFormShowsForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/editAccountForm"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/EditAccountForm"));
    }

    // 잘못된 CSRF 토큰으로 수정하면 다시 폼을 보여준다
    @Test
    void editAccountInvalidCsrfShowsForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        session.setAttribute("csrf_token", "token");
        mockMvc.perform(MockMvcRequestBuilders.post("/editAccount")
                .session(session)
                .param("csrf", "bad"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/EditAccountForm"));
    }

    // 올바른 CSRF 토큰으로 수정하면 메인 화면으로 이동한다
    @Test
    void editAccountValidCsrfRedirects() throws Exception {
        Account account = new Account();
        account.setUsername("user");
        account.setFavouriteCategoryId("CAT");
        when(httpFacade.getProductListByCategory("CAT")).thenReturn(java.util.Collections.emptyList());
        when(accountService.getAccount("user")).thenReturn(account);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", account);
        session.setAttribute("csrf_token", "token");

        mockMvc.perform(MockMvcRequestBuilders.post("/editAccount")
                .session(session)
                .param("csrf", "token")
                .param("username", "user"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 로그인된 상태에서 로그인 폼을 요청하면 메인으로 이동한다
    @Test
    void signonFormWhenLoggedInRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        mockMvc.perform(MockMvcRequestBuilders.get("/signonForm").session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 비로그인 상태에서 로그인 폼을 요청하면 폼을 반환한다
    @Test
    void signonFormWhenNotLoggedInShowsForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signonForm"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/SignonForm"));
    }

    // 로그아웃하면 메인 화면으로 이동한다
    @Test
    void signoffRedirectsToMain() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        mockMvc.perform(MockMvcRequestBuilders.get("/signoff").session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 로그인 상태에서 새 계정 폼을 요청하면 메인 화면으로 이동한다
    @Test
    void newAccountFormWhenLoggedInRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", new Account());
        mockMvc.perform(MockMvcRequestBuilders.get("/newAccountForm").session(session))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 비로그인 상태에서 새 계정 폼을 요청하면 폼을 반환한다
    @Test
    void newAccountFormWhenNotLoggedInShowsForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/newAccountForm"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/NewAccountForm"));
    }

    // 올바른 사용자 정보로 로그인하면 메인 화면으로 이동한다
    @Test
    void signonWithValidUserRedirects() throws Exception {
        Account account = new Account();
        when(accountService.getAccount("user", "pass")).thenReturn(account);
        when(httpFacade.getProductListByCategory((String)org.mockito.ArgumentMatchers.any())).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/signon")
                .param("username", "user")
                .param("password", "pass"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/catalog"));
    }

    // 잘못된 사용자 정보로 로그인하면 로그인 폼을 다시 보여준다
    @Test
    void signonWithInvalidUserShowsForm() throws Exception {
        when(accountService.getAccount("bad", "bad")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/signon")
                .param("username", "bad")
                .param("password", "bad"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account/SignonForm"));
    }
}
