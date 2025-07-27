package org.mybatis.jpetstore.http;

import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HttpFacade {
    @Autowired
    RestTemplate restTemplate;
    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    public boolean updateInventoryQuantity(Map<String, Object> param, Integer orderId) {

        String quantityString = param.values().stream().map(String::valueOf).collect(Collectors.joining(","));

        String url = gatewayBaseUrl + "/catalog/updateQuantity?itemId=" + String.join(",", param.keySet()) + "&increment=" + quantityString + "&orderId=" + orderId;

        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url, Boolean.class);
        Boolean responses = responseEntity.getBody();

        return responses;
    }

    public boolean isInventoryUpdateCommitSuccess(Integer orderId){
        String url = gatewayBaseUrl + "/catalog/isInventoryUpdated?orderId=" + orderId;
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url,Boolean.class);
        return Boolean.TRUE.equals(responseEntity.getBody());
    }

    public Item getItem(String itemId) {
        String url = gatewayBaseUrl + "/catalog/getItem?itemId=" + itemId;

        ResponseEntity<Item> responseEntity = restTemplate.getForEntity(url, Item.class);
        Item response = responseEntity.getBody();
        return response;
    }

    public int getInventoryQuantity(String itemId) {
        String url = gatewayBaseUrl + "/catalog/getQuantity?itemId=" + itemId;

        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url, Integer.class);
        int response = responseEntity.getBody();
        return response;
    }
}
