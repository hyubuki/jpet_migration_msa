package org.mybatis.jpetstore.controller;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService){
        this.catalogService = catalogService;
    }

    /**
     * 메인 페이지를 보여줍니다.
     */
    @GetMapping("/")
    public String viewMain(){
        return "catalog/Main";
    }

    /**
     * 카테고리 상세 페이지를 보여줍니다.
     *
     * @param categoryId 카테고리 ID
     * @param model 뷰 모델
     * @return 뷰 이름
     */
    @GetMapping("/category")
    public String viewCategory(@RequestParam String categoryId, Model model){
        // 카테고리 조회
        Category category = catalogService.getCategory(categoryId);
        // 상품 목록 조회
        List<Product> productList = catalogService.getProductListByCategory(categoryId);
        // 모델 설정
        model.addAttribute("category",category);
        model.addAttribute("products",productList);
        return "catalog/Category";
    }

    /**
     * 상품 상세 페이지를 보여줍니다.
     */
    @GetMapping("/product")
    public String viewProduct(@RequestParam String productId, Model model){
        // 상품 조회
        Product product = catalogService.getProduct(productId);
        // 해당 상품의 아이템 목록 조회
        List<Item> itemList = catalogService.getItemListByProduct(productId);
        // 모델 설정
        model.addAttribute("product",product);
        model.addAttribute("itemList",itemList);
        // 뷰 반환
        return "catalog/Product";
    }

    /**
     * 아이템 상세 페이지를 보여줍니다.
     */
    @GetMapping("/item")
    public String viewItem(@RequestParam String itemId, Model model){
        // 아이템 조회
        Item item = catalogService.getItem(itemId);
        // 아이템이 속한 상품 조회
        Product product = item.getProduct();
        // 모델 설정
        model.addAttribute("item",item);
        model.addAttribute("product",product);
        return "catalog/Item";
    }

    /**
     * 입력한 키워드로 상품을 검색합니다.
     */
    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam String keywords, Model model){
        if(keywords == null ||  keywords.length() < 1){
            String errorMessage = "검색어를 입력한 후 검색 버튼을 눌러주세요.";
            model.addAttribute("message",errorMessage);
        }else{
            List<Product> productList = catalogService.searchProductList(keywords.toLowerCase());
            model.addAttribute("productList",productList);
        }
        return "catalog/SearchProducts";
    }

    /**
     * 지정한 카테고리의 상품 목록을 반환합니다.
     */
    @ResponseBody
    @PostMapping("/get/productList")
    public List<Product> getProductList(@RequestParam String catalogId){
        return catalogService.getProductListByCategory(catalogId);
    }

    /**
     * 아이템 재고 여부를 확인합니다.
     */
    @ResponseBody
    @GetMapping("/isItemInStock")
    public Boolean getIsItemInStock(@RequestParam String itemId){
        return catalogService.isItemInStock(itemId);
    }

    /**
     * 아이템 정보를 반환합니다.
     */
    @ResponseBody
    @GetMapping("/getItem")
    public Item getItem(@RequestParam String itemId) {
        return catalogService.getItem(itemId);
    }

    /**
     * 아이템의 현재 재고 수량을 반환합니다.
     */
    @ResponseBody
    @GetMapping("/getQuantity")
    public ResponseEntity<Integer> getInventoryQuantity(@RequestParam String itemId){
        Integer quantity = catalogService.getItemQuantity(itemId);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }

    /**
     * 여러 아이템의 재고 수량을 반환합니다.
     */
    @ResponseBody
    @GetMapping("/getQuantities")
    public List<Integer> getInventoryQuantities(@RequestBody List<String> itemIds){
        return catalogService.getInventoryQuantities(itemIds);
    }

    /**
     * 주문에 대한 재고 수량을 업데이트합니다.
     */
    @ResponseBody
    @GetMapping("/updateQuantity")
    public ResponseEntity<Boolean> updateInventoryQuantity(@RequestParam List<String> itemId, @RequestParam List<Integer> increment, @RequestParam Integer orderId){
        try{
            catalogService.updateItemQuantity(itemId,increment,orderId);
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK); // 테스트용 FALSE 처리
        }catch (Exception e){
            return new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK);
        }
    }


    /**
     * 주문 처리 시 재고가 정상적으로 업데이트되었는지 확인합니다.
     */
    @ResponseBody
    @GetMapping("/isInventoryUpdated")
    public ResponseEntity<Boolean> isInventoryUpdatedSuccess(@RequestParam Integer orderId) throws InterruptedException {
//        Thread.sleep(50000); // unknoown_case2_timeout 테스트를 위해 의도적으로 50초의 sleep 발생시킴
        Boolean isCommitSuccess = catalogService.isInventoryUpdateSuccess(orderId);
        return new ResponseEntity<Boolean>(isCommitSuccess,HttpStatus.OK);

    }

    /**
     * 주문 실패 시 재고를 복구하기 위한 Kafka 리스너입니다.
     */
    @KafkaListener(topics="product_compensation", groupId = "group_1")
    public void incrInventoryQuantity(HashMap<String, Object> data) {
        catalogService.rollbackInventory(data);
    }

}
