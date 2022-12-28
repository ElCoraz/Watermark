package com.elcorazon.adminlte.utils;

import com.elcorazon.adminlte.model.Search;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static ResponseEntity<String> query(String url, HttpMethod httpMethod, Search searh) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Basic " + authorization);
        if (httpMethod == HttpMethod.GET) {
            return restTemplate.exchange(path + url, httpMethod, new HttpEntity<>(httpHeaders), String.class);
        } else if (httpMethod == HttpMethod.POST) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(searh), httpHeaders);
            return restTemplate.postForEntity(path + url, entity, String.class);
        }

        return null;
    }
    /******************************************************************************************************************/
    public static ResponseEntity<String> getResponseEntityQuery(String url, HttpMethod httpMethod, Search search) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(query(url, httpMethod, search).getBody());
    }
    /******************************************************************************************************************/
    public static  String getStringQuery(String url, HttpMethod httpMethod) throws JsonProcessingException {
        return query(url, httpMethod, null).getBody();
    }
}
