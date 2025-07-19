package org.mybatis.jpetstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mybatis.jpetstore.controller.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.mybatis.jpetstore.service.CatalogService;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.Item;

import java.util.Collections;

import static org.mockito.Mockito.when;

@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CatalogService catalogService;

    // 메인 페이지 요청 시 정상적으로 뷰를 반환한다
    @Test
    void viewMainReturnsMainPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/Main"));
    }

    // 카테고리 조회 시 상품 목록을 모델에 담아 반환한다
    @Test
    void viewCategoryShowsProducts() throws Exception {
        when(catalogService.getCategory("CAT")).thenReturn(new Category());
        when(catalogService.getProductListByCategory("CAT")).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/category").param("categoryId", "CAT"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/Category"));
    }

    // 검색어가 없을 때는 오류 메시지를 포함하여 검색 화면을 보여준다
    @Test
    void searchProductsWithoutKeywordShowsError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/searchProducts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/SearchProducts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"));
    }

    // 검색어가 있으면 결과 목록을 모델에 포함한다
    @Test
    void searchProductsReturnsResults() throws Exception {
        when(catalogService.searchProductList("dog")).thenReturn(Collections.singletonList(new Product()));
        mockMvc.perform(MockMvcRequestBuilders.get("/searchProducts").param("keywords", "dog"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/SearchProducts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productList"));
    }

    // categoryId 파라미터가 없으면 400 오류를 반환한다
    @Test
    void viewCategoryWithoutIdReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 상품 상세 페이지를 정상적으로 보여준다
    @Test
    void viewProductShowsProduct() throws Exception {
        when(catalogService.getProduct("P1")).thenReturn(new Product());
        when(catalogService.getItemListByProduct("P1")).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/product").param("productId", "P1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/Product"));
    }

    // 아이템 상세 페이지를 정상적으로 보여준다
    @Test
    void viewItemShowsItem() throws Exception {
        Product product = new Product();
        Item item = new Item();
        item.setProduct(product);
        when(catalogService.getItem("I1")).thenReturn(item);
        mockMvc.perform(MockMvcRequestBuilders.get("/item").param("itemId", "I1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("catalog/Item"));
    }

    // 재고 확인 API는 불린 값을 반환한다
    @Test
    void isItemInStockReturnsBoolean() throws Exception {
        when(catalogService.isItemInStock("I1")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/isItemInStock").param("itemId", "I1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
