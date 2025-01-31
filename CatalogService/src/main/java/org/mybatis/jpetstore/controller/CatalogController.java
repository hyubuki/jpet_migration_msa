package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Controller
@RequestMapping("/")
public class CatalogController {

    @Autowired
    private final CatalogService catalogService;
    public CatalogController(CatalogService catalogService){
        this.catalogService = catalogService;
    }

    @GetMapping("/")
    public String viewMain(){
        return "catalog/Main";
    }

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

    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam String keywords, Model model){
        if(keywords == null ||  keywords.length() < 1){
            String errorMessage = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("message",errorMessage);
        }else{
            List<Product> productList = catalogService.searchProductList(keywords.toLowerCase());
            model.addAttribute("productList",productList);
        }
        return "catalog/SearchProducts";
    }

    @ResponseBody
    @PostMapping("/get/productList")
    public List<Product> getProductList(@RequestParam String catalogId){
        List<Product> productListByCategory = catalogService.getProductListByCategory(catalogId);
        return productListByCategory;
    }

    @ResponseBody
    @GetMapping("/isItemInStock")
    public Boolean getIsItemInStock(@RequestParam String itemId){
        Boolean isItemInStock = catalogService.isItemInStock(itemId);
        return isItemInStock;
    }

    @ResponseBody
    @GetMapping("/getItem")
    public Item getItem(@RequestParam String itemId) {
        return catalogService.getItem(itemId);
    }

    @ResponseBody
    @GetMapping("/getQuantity")
    public ResponseEntity<Integer> getInventoryQuantity(@RequestParam String itemId){
        Integer quantity = catalogService.getItemQuantity(itemId);
        return new ResponseEntity<Integer>(quantity,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/updateQuantity")
    public ResponseEntity<Boolean> updateInventoryQuantity(@RequestParam String itemId, @RequestParam Integer increment){
        Boolean isUpdated = catalogService.updateItemQuantity(itemId,increment);
        return new ResponseEntity<Boolean>(isUpdated,HttpStatus.OK);
    }


}
