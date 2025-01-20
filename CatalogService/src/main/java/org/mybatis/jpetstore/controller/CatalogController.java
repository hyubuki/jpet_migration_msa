package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class CatalogController {

    @Autowired
    private final CatalogService catalogService;
    public CatalogController(CatalogService catalogService){
        this.catalogService = catalogService;
    }

    // TODO : 홈 화면으로 이동되어야 합니다.
    @GetMapping("/main")
    public String viewMain(HttpSession session){
        // TODO : orderservice _ getAccount()
        // TODO : 로그인 여부 => redis session에서 각 서비스가 가져다 쓰는건지?
        // orderSer
        return "catalog/Main";
    }

    // TODO : viewCategory(categoryId): 카테고리 ID에 해당하는 상품 목록을 볼 수 있는 페이지로 이동되어야 합니다.
    @GetMapping("/category")
    public String viewCategory(@RequestParam String categoryId, Model model){
        // category
        Category category = catalogService.getCategory(categoryId);
        // product name
        List<Product> productList = catalogService.getProductListByCategory(categoryId);
        // set model
        model.addAttribute("category",category);
        model.addAttribute("products",productList);
        return "catalog/Category";
    }

    @GetMapping("/product")
    public String viewProduct(@RequestParam String productId, Model model){
        // product by productId
        Product product = catalogService.getProduct(productId);
        // Product
        List<Item> itemList = catalogService.getItemListByProduct(productId);
        // set model
        model.addAttribute("product",product);
        model.addAttribute("itemList",itemList);
        // ItemList by productId
        return "catalog/Product";
    }

    @GetMapping("/item")
    public String viewItem(@RequestParam String itemId, Model model){
        // item
        Item item = catalogService.getItem(itemId);
        // product of item
        Product product = item.getProduct();
        // set model
        model.addAttribute("item",item);
        model.addAttribute("product",product);
        return "catalog/Item";
    }

    // TODO : searchProducts(keyword): Keyword로 상품을 검색한 결과 페이지로 이동되어야 합니다.
    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam String keywords,Model model){
        if(keywords == null ||  keywords.length() < 1){
            String errorMessage = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("message",errorMessage);
        }else{
            List<Product> productList = catalogService.searchProductList(keywords.toLowerCase());
            model.addAttribute("productList",productList);
        }
        return "catalog/SearchProducts";
    }

    // TODO : getProductLIst(catalogId) _ Json 형태로 카테고리 ID에 해당하는 Product 객체를 여러 개 받습니다.
    @ResponseBody
    @PostMapping("/get/productList")
    public List<Product> getProductList(@RequestParam String catalogId){
        List<Product> productListByCategory = catalogService.getProductListByCategory(catalogId);
        return productListByCategory;
    }

}
