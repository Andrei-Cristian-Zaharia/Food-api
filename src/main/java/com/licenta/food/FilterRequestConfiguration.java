package com.licenta.food;

import com.licenta.food.filters.RouteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRequestConfiguration {

    private final FoodConfigurations foodConfigurations;

    public FilterRequestConfiguration(FoodConfigurations foodConfigurations) {
        this.foodConfigurations = foodConfigurations;
    }

    @Bean
    public FilterRegistrationBean<RouteFilter> filterRoute() {

        FilterRegistrationBean<RouteFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new RouteFilter(foodConfigurations));

        return filter;
    }
}
