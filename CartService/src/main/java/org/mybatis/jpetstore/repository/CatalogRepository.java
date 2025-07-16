package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.HttpRequest.HttpGetRequest;
import org.mybatis.jpetstore.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 카탈로그 서비스와 통신하여 상품 정보를 조회합니다.
 */
@Repository
public class CatalogRepository {

    @Autowired
    private HttpGetRequest httpGetRequest;

    /**
     * 지정한 ID의 상품을 조회합니다.
     *
     * @param itemId 상품 ID
     * @return 상품 정보
     */
    public Item findItem(String itemId) {
        return httpGetRequest.getItemFromCatalogService(itemId);
    }

    /**
     * 아이템이 재고에 존재하는지 확인합니다.
     *
     * @param itemId 상품 ID
     * @return 재고 존재 여부
     */
    public boolean isItemInStock(String itemId) {
        Boolean result = httpGetRequest.isItemInStockFromCatalogService(itemId);
        return Boolean.TRUE.equals(result);
    }
}
