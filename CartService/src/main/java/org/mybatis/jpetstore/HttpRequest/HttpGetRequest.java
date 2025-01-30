package org.mybatis.jpetstore.HttpRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.jpetstore.domain.Item;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest {
    public static Item getItemFromCatalogService(String itemId) {
        // TODO : 배포시 해당 URL로 변경
        try {
            // url 설정
            URL url = new URL(String.format("http://localhost:8080/catalog/getItem?itemId=%s",itemId));
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


    public static void isItemInStockFromCatalogService(String itemId) {
        // TODO : 배포시 해당 URL로 변경
        try {
            // url 설정
            URL url = new URL(String.format("http://localhost:8080/catalog/isItemInStock?itemId=%s",itemId));
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

                System.out.println(response.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
