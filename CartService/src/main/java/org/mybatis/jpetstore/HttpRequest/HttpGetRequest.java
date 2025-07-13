package org.mybatis.jpetstore.HttpRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.jpetstore.domain.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HttpGetRequest {

    @Value("${gateway.base-url}")
    private String gatewayBaseUrl;

    public Item getItemFromCatalogService(String itemId) {
        // TODO : 배포시 해당 URL로 변경
        try {
            // url 설정
            URL url = new URL(String.format("%s/catalog/getItem?itemId=%s", gatewayBaseUrl, itemId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 요청 방식 설정 (GET)
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            // 응답 읽기
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON 응답을 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                Item item = objectMapper.readValue(response.toString(), Item.class);

                return item;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Boolean isItemInStockFromCatalogService(String itemId) {
        // TODO : 배포시 해당 URL로 변경
        try {
            // url 설정
            URL url = new URL(String.format("%s/catalog/isItemInStock?itemId=%s", gatewayBaseUrl, itemId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 요청 방식 설정 (GET)
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            // 응답 읽기
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString().equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
