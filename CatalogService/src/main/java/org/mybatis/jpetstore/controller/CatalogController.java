package org.mybatis.jpetstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
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
    @GetMapping("/getQuantities")
    public List<Integer> getInventoryQuantities(@RequestBody List<String> itemIds){

        List<Integer>quantityOfItems = new ArrayList<>();
        for (String itemId : itemIds){
            Integer quantity = catalogService.getItemQuantity(itemId); // 후에 단일 쿼리 조회로 가져올 수 있게 변경 예정
            quantityOfItems.add(quantity);
        }
        return quantityOfItems;
    }

    @ResponseBody
    @GetMapping("/updateQuantity")
    public ResponseEntity<Boolean> updateInventoryQuantity(@RequestParam List<String> itemId, @RequestParam List<Integer> increment, @RequestParam Integer orderId){
        try{
            catalogService.updateItemQuantity(itemId,increment,orderId);
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK); // 오류 발생 의도하기 위해 FLASE로 변경, 정상 로직은 True 반환이 맞음
        }catch (Exception e){
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK);
        }
    }


    @ResponseBody
    @GetMapping("/isInventoryUpdated")
    public ResponseEntity<Boolean> isInventoryUpdatedSuccess(@RequestParam Integer orderId){

        Boolean isCommitSuccess = catalogService.isInventoryUpdateSuccess(orderId);
        return new ResponseEntity<Boolean>(isCommitSuccess,HttpStatus.OK);

    }

    @KafkaListener(topics="prod_compensation", groupId = "group_1")
    public void incrInventoryQuantity(HashMap<String, Object> data) {
        List<String> itemId = new ArrayList<>();
        List<Integer> increment = new ArrayList<>();

        for (String key : data.keySet()) {
            itemId.add(key);
            increment.add((int) data.get(key));
        }
        catalogService.rollBackInventoryQuantity(itemId, increment);
    }

}
