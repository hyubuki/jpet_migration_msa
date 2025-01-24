package org.mybatis.jpetstore.http;

import org.mybatis.jpetstore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class HttpFacade {
    @Autowired
    RestTemplate restTemplate;

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/catalog";

    public List<Product> getProductListByCategory(String categoryId) {
        String url = CATALOG_SERVICE_URL + "/get/productList?catalogId=" + categoryId;

        ResponseEntity<Product[]> responseEntity = restTemplate.postForEntity(url, null, Product[].class);
        Product[] responses = responseEntity.getBody();
        List<Product> productList = Arrays.asList(responses);

        return productList;
    }
}
