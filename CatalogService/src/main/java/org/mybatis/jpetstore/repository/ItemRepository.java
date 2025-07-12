package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Item} 엔티티 조회와 재고 업데이트를 담당하는 레포지터리입니다.
 */
@Repository
public class ItemRepository {

    private final ItemMapper mapper;

    @Autowired
    public ItemRepository(ItemMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 재고 수정을 위해 아이템을 잠급니다.
     *
     * @param itemIds 아이템 ID 목록
     */
    public void lockForUpdate(List<String> itemIds) {
        mapper.lockItemsForUpdate(itemIds);
    }

    /**
     * 주어진 아이템의 재고 수량을 갱신합니다.
     *
     * @param itemId 아이템 ID
     * @param increment 증가할 수량
     */
    public void updateInventoryQuantity(String itemId, Integer increment) {
        mapper.updateInventoryQuantity(itemId, increment);
    }

    /**
     * 아이템의 재고 수량을 되돌립니다.
     *
     * @param itemId 아이템 ID
     * @param increment 되돌릴 수량
     */
    public void rollBackInventoryQuantity(String itemId, Integer increment) {
        mapper.rollBackInventoryQuantity(itemId, increment);
    }

    /**
     * 현재 아이템의 재고 수량을 조회합니다.
     *
     * @param itemId 아이템 ID
     * @return 현재 수량
     */
    public int getInventoryQuantity(String itemId) {
        return mapper.getInventoryQuantity(itemId);
    }

    /**
     * 특정 상품에 속한 아이템 목록을 조회합니다.
     *
     * @param productId 상품 ID
     * @return 아이템 목록
     */
    public List<Item> findByProduct(String productId) {
        return mapper.getItemListByProduct(productId);
    }

    /**
     * ID로 아이템을 조회합니다.
     *
     * @param itemId 아이템 ID
     * @return 아이템 또는 없으면 {@code null}
     */
    public Item findById(String itemId) {
        return mapper.getItem(itemId);
    }
}
