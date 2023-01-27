package com.licenta.food;

import org.springframework.web.util.UriComponentsBuilder;

public class ApiConfig {

    private static final Integer RESTAURANT_PORT = 3002;

    private static final Integer CORE_PORT = 3001;

    private static final String SCHEME = "http";

    private static final String HOST = "localhost";

    private ApiConfig() {}

    public static UriComponentsBuilder restaurantApiPath() {
        return UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(RESTAURANT_PORT)
                .path("v1/restaurant-api");
    }

    public static UriComponentsBuilder coreApiPath() {
        return UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(CORE_PORT)
                .path("v1/core-api");
    }
}
