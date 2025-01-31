package org.mybatis.jpetstore.http;

import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class HttpFacade {
    @Autowired
    RestTemplate restTemplate;

    private static final String CATALOG_SERVICE_URL = "http://localhost:8080/catalog";

    public boolean updateInventoryQuantity(Map<String, Object> param) {
        String url = CATALOG_SERVICE_URL + "/updateQuantity?itemId=" + param.get("itemId") + "&increment=" + param.get("increment");

        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url, Boolean.class);
        Boolean responses = responseEntity.getBody();

        return responses;
    }

    public Item getItem(String itemId) {
        String url = CATALOG_SERVICE_URL + "/getItem?itemId=" + itemId;

        ResponseEntity<Item> responseEntity = restTemplate.getForEntity(url, Item.class);
        Item response = responseEntity.getBody();
        return response;
    }

    public int getInventoryQuantity(String itemId) {
        String url = CATALOG_SERVICE_URL + "/getQuantity?itemId=" + itemId;

        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url, Integer.class);
        int response = responseEntity.getBody();
        return response;
    }
}
