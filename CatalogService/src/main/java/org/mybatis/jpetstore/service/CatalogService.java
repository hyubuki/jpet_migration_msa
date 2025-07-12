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
import org.mybatis.jpetstore.repository.CategoryRepository;
import org.mybatis.jpetstore.repository.InventoryUpdateStatusRepository;
import org.mybatis.jpetstore.repository.ItemRepository;
import org.mybatis.jpetstore.repository.ProductRepository;
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

  private final CategoryRepository categoryRepository;
  private final ItemRepository itemRepository;
  private final ProductRepository productRepository;
  private final InventoryUpdateStatusRepository inventoryUpdateStatusRepository;

  @Autowired
  /**
   * 필수 저장소를 주입하여 {@code CatalogService} 인스턴스를 생성합니다.
   *
   * @param categoryRepository 카테고리 저장소
   * @param itemRepository 아이템 저장소
   * @param productRepository 상품 저장소
   * @param inventoryUpdateStatusRepository 재고 업데이트 상태 저장소
   */
  public CatalogService(CategoryRepository categoryRepository, ItemRepository itemRepository, ProductRepository productRepository, InventoryUpdateStatusRepository inventoryUpdateStatusRepository) {
    this.categoryRepository = categoryRepository;
    this.itemRepository = itemRepository;
    this.productRepository = productRepository;
    this.inventoryUpdateStatusRepository = inventoryUpdateStatusRepository;
  }

  /**
   * 모든 카테고리 목록을 조회합니다.
   *
   * @return 카테고리 목록
   */
  public List<Category> getCategoryList() {
    return categoryRepository.findAll();
  }

  /**
   * ID로 카테고리를 조회합니다.
   *
   * @param categoryId 카테고리 ID
   * @return 카테고리 또는 없으면 {@code null}
   */
  public Category getCategory(String categoryId) {
    return categoryRepository.findById(categoryId);
  }

  /**
   * ID로 상품을 조회합니다.
   *
   * @param productId 상품 ID
   * @return 상품 객체 또는 없으면 {@code null}
   */
  public Product getProduct(String productId) {
    return productRepository.findById(productId);
  }

  /**
   * 지정한 카테고리에 속한 상품 목록을 조회합니다.
   *
   * @param categoryId 카테고리 ID
   * @return 상품 목록
   */
  public List<Product> getProductListByCategory(String categoryId) {
    return productRepository.findByCategory(categoryId);
  }

  /**
   * 키워드를 포함하는 상품을 검색합니다.
   *
   * @param keywords 공백으로 구분된 검색어
   * @return 검색된 상품 목록
   */
  public List<Product> searchProductList(String keywords) {
    return Arrays.stream(keywords.split("\\s+"))
        .map(k -> "%" + k.toLowerCase() + "%")
        .map(productRepository::search)
        .flatMap(Collection::stream)
        .toList();
  }

  /**
   * 특정 상품에 속한 아이템 목록을 조회합니다.
   *
   * @param productId 상품 ID
   * @return 아이템 목록
   */
  public List<Item> getItemListByProduct(String productId) {
    return itemRepository.findByProduct(productId);
  }

  /**
   * ID로 아이템을 조회합니다.
   *
   * @param itemId 아이템 ID
   * @return 아이템 객체 또는 없으면 {@code null}
   */
  public Item getItem(String itemId) {
    return itemRepository.findById(itemId);
  }

  /**
   * 아이템이 재고에 존재하는지 확인합니다.
   *
   * @param itemId 아이템 ID
   * @return 수량이 0보다 크면 {@code true}
   */
  public boolean isItemInStock(String itemId) {
    return itemRepository.getInventoryQuantity(itemId) > 0;
  }

  /**
   * 여러 아이템의 재고 수량을 변경하고 상태를 기록합니다.
   *
   * @param itemId   아이템 ID 목록
   * @param increment 각각 증가할 수량
   * @param orderId  관련 주문 ID
   * @throws Exception 잠금이나 업데이트 실패 시 발생
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateItemQuantity(List<String> itemId, List<Integer> increment, Integer orderId) throws Exception{

      // lock 획득
      itemRepository.lockForUpdate(itemId);

      // 각각 업데이트
      for(int i = 0; i < itemId.size(); i++) {
        itemRepository.updateInventoryQuantity(itemId.get(i), increment.get(i));
      }
      // 성공 여부 업데이트
      inventoryUpdateStatusRepository.insertStatus(new InventoryUpdateStatus(orderId));
  }

  /**
   * 주어진 주문의 재고 업데이트 성공 여부를 확인합니다.
   *
   * @param orderId 주문 ID
   * @return 업데이트가 완료되었으면 {@code true}
   */
  public boolean isInventoryUpdateSuccess(Integer orderId){
    Optional<InventoryUpdateStatus> inventoryUpdateStatus = inventoryUpdateStatusRepository.findByOrderId(orderId);
    if (inventoryUpdateStatus.isPresent()) return true;
    else return false;
  }

  /**
   * 지정한 아이템들의 재고 수량을 되돌립니다.
   *
   * @param itemId   아이템 ID 목록
   * @param increment 복구할 수량
   */
  @Transactional
  public void rollBackInventoryQuantity(List<String> itemId, List<Integer> increment) {
    try {
      // lock 획득
      itemRepository.lockForUpdate(itemId);

      for (int i = 0; i < itemId.size(); i++) {
        itemRepository.rollBackInventoryQuantity(itemId.get(i), increment.get(i));
      }

    } catch (Exception e) {
      // 보상 트랜잭션 실패
      System.out.println(e.getMessage());
    }
  }

  /**
   * 여러 아이템의 재고 수량을 조회합니다.
   *
   * @param itemIds 아이템 ID 목록
   * @return 각 아이템의 현재 수량 목록
   */
  public List<Integer> getInventoryQuantities(List<String> itemIds){
    return itemIds.stream()
            .map(this::getItemQuantity)
            .toList();
  }

  /**
   * 재고 복구 요청 데이터를 처리합니다.
   *
   * @param data 복구할 아이템과 수량 정보
   */
  public void rollbackInventory(Map<String, Object> data){
    List<String> itemId = new ArrayList<>();
    List<Integer> increment = new ArrayList<>();
    for (String key : data.keySet()) {
      itemId.add(key);
      increment.add((Integer) data.get(key));
    }
    rollBackInventoryQuantity(itemId, increment);
  }

  /**
   * 특정 아이템의 재고 수량을 조회합니다.
   *
   * @param itemId 아이템 ID
   * @return 현재 수량
   */
  public Integer getItemQuantity(String itemId){
    Integer quantity = itemRepository.getInventoryQuantity(itemId);
    return quantity;
  }

}
