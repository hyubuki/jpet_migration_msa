/*
 *    Copyright 2010-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.service;

import java.util.*;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.InventoryUpdateStatus;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.mapper.CategoryMapper;
import org.mybatis.jpetstore.mapper.InventoryUpdateStatusMapper;
import org.mybatis.jpetstore.mapper.ItemMapper;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class CatalogService.
 *
 * @author Eduardo Macarron
 */
@Service
public class CatalogService {

  private final CategoryMapper categoryMapper;
  private final ItemMapper itemMapper;
  private final ProductMapper productMapper;
  private final InventoryUpdateStatusMapper inventoryUpdateStatusMapper;

  @Autowired
  public CatalogService(CategoryMapper categoryMapper, ItemMapper itemMapper, ProductMapper productMapper, InventoryUpdateStatusMapper inventoryUpdateStatusMapper) {
    this.categoryMapper = categoryMapper;
    this.itemMapper = itemMapper;
    this.productMapper = productMapper;
    this.inventoryUpdateStatusMapper = inventoryUpdateStatusMapper;
  }

  public List<Category> getCategoryList() {
    return categoryMapper.getCategoryList();
  }

  public Category getCategory(String categoryId) {
    return categoryMapper.getCategory(categoryId);
  }

  public Product getProduct(String productId) {
    return productMapper.getProduct(productId);
  }

  public List<Product> getProductListByCategory(String categoryId) {
    return productMapper.getProductListByCategory(categoryId);
  }

  /**
   * Search product list.
   *
   * @param keywords
   *          the keywords
   *
   * @return the list
   */
  public List<Product> searchProductList(String keywords) {
    List<Product> products = new ArrayList<>();
    for (String keyword : keywords.split("\\s+")) {
      products.addAll(productMapper.searchProductList("%" + keyword.toLowerCase() + "%"));
    }
    return products;
  }

  public List<Item> getItemListByProduct(String productId) {
    return itemMapper.getItemListByProduct(productId);
  }

  public Item getItem(String itemId) {
    return itemMapper.getItem(itemId);
  }

  public boolean isItemInStock(String itemId) {
    return itemMapper.getInventoryQuantity(itemId) > 0;
  }


  @Transactional
  public boolean updateItemQuantity(List<String> itemId, List<Integer> increment, String orderId){
    try{
      // lock 획득
      itemMapper.lockItemsForUpdate(itemId);

      // 각각 업데이트
      for(int i = 0; i < itemId.size(); i++) {
        itemMapper.updateInventoryQuantity(itemId.get(i), increment.get(i));
      }
      // 성공 여부 업데이트
      inventoryUpdateStatusMapper.insertInventoryUpdateStatus(new InventoryUpdateStatus(orderId));

    }catch (Exception e){
      return false;
    }
    return true;
  }

  public boolean isInventoryUpdateSuccess(Integer orderId){
    Optional<InventoryUpdateStatus> inventoryUpdateStatus = inventoryUpdateStatusMapper.getInventoryUpdateStatusByOrderId(orderId);
    if (inventoryUpdateStatus.isPresent()) return true;
    else return false;
  }

  @Transactional
  public void rollBackInventoryQuantity(List<String> itemId, List<Integer> increment) {
    try {
      // lock 획득
      itemMapper.lockItemsForUpdate(itemId);

      for (int i = 0; i < itemId.size(); i++) {
        itemMapper.rollBackInventoryQuantity(itemId.get(i), increment.get(i));
      }
    } catch (Exception e) {
      // 보상 트랜잭션 실패
      System.out.println(e.getMessage());
    }
  }

  public Integer getItemQuantity(String itemId){
    Integer quantity = itemMapper.getInventoryQuantity(itemId);
    return quantity;
  }

}
