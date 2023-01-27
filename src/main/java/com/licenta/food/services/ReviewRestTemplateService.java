package com.licenta.food.services;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.licenta.food.ApiConfig;

import java.net.URI;
import java.util.List;

@Service
public class ReviewRestTemplateService {

    private static final String REVIEW_ROUTE = "/review/";

    private final RestTemplate restTemplate;

    public ReviewRestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Integer getRatingForRecipe(Long id) throws JSONException {
        URI uri = ApiConfig.coreApiPath()
                .path(REVIEW_ROUTE)
                .path("rating")
                .build()
                .toUri();

        JSONObject body = new JSONObject();
        body.put("id", id);
        body.put("category", "RECIPE");

        HttpEntity<String> entityCredentials = new HttpEntity<>(body.toString(), createHeaderBody());

        return restTemplate.exchange(uri, HttpMethod.POST, entityCredentials, Integer.class).getBody();
    }

    private static HttpHeaders createHeaderBody() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("authorities", "[PERMIT]");

        return httpHeaders;
    }
}
