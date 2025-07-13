package org.mybatis.jpetstore.http;

import org.mybatis.jpetstore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
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

    public List<Product> getProductListByCategory(HttpServletRequest req, String categoryId) {
        String url = getBaseUrl(req) + "/catalog/get/productList?catalogId=" + categoryId;

        ResponseEntity<Product[]> responseEntity = restTemplate.postForEntity(url, null, Product[].class);
        Product[] responses = responseEntity.getBody();
        List<Product> productList = Arrays.asList(responses);

        return productList;
    }

    public String getBaseUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder
                .fromRequestUri(request)    // scheme + host + port + path
                .replacePath(null)          // path 제거
                .replaceQuery(null)         // query 제거 (필요없다면 생략)
                .build()
                .toUriString();             // "http://example.com:8080"
    }
}
