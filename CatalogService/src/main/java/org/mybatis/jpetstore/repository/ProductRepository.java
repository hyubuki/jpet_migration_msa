package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Product} 엔티티에 접근하기 위한 레포지터리입니다.
 */
@Repository
public class ProductRepository {

    private final ProductMapper mapper;

    @Autowired
    public ProductRepository(ProductMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 상품 목록
     */
    public List<Product> findByCategory(String categoryId) {
        return mapper.getProductListByCategory(categoryId);
    }

    /**
     * 주어진 ID의 상품을 반환합니다.
     *
     * @param productId 상품 ID
     * @return 상품 또는 없으면 {@code null}
     */
    public Product findById(String productId) {
        return mapper.getProduct(productId);
    }

    /**
     * 주어진 키워드로 상품을 검색합니다.
     *
     * @param keyword 검색어
     * @return 검색된 상품 목록
     */
    public List<Product> search(String keyword) {
        return mapper.searchProductList(keyword);
    }
}
