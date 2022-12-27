package com.elcorazon.adminlte.utils;

import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
/**********************************************************************************************************************/
public class Query {
    /******************************************************************************************************************/
    public static String path = "http://192.168.0.136/torgupr/hs/marketing/";
    /******************************************************************************************************************/
    private static String authorization = "YWRtaW46NDIxNzc3Nw==";
    /******************************************************************************************************************/
    private static ResponseEntity<String> query(String url, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Basic " + authorization);

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(path + url, httpMethod, httpEntity, String.class);
    }
    /******************************************************************************************************************/
    public static ResponseEntity<String> getResponseEntityQuery(String url, HttpMethod httpMethod) {
        return ResponseEntity.status(HttpStatus.OK).body( query(url, httpMethod).getBody());
    }
    /******************************************************************************************************************/
    public static  String getStringQuery(String url, HttpMethod httpMethod) {
        return query(url, httpMethod).getBody();
    }
}
