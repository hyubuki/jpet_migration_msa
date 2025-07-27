package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Category} 엔티티에 접근하기 위한 레포지터리입니다.
 */
@Repository
public class CategoryRepository {

    private final CategoryMapper mapper;

    @Autowired
    public CategoryRepository(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 모든 카테고리를 조회합니다.
     *
     * @return 카테고리 목록
     */
    public List<Category> findAll() {
        return mapper.getCategoryList();
    }

    /**
     * 주어진 ID에 해당하는 카테고리를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 혹은 없으면 {@code null}
     */
    public Category findById(String categoryId) {
        return mapper.getCategory(categoryId);
    }
}
