package com.licenta.food;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "food")
public class FoodConfigurations {

    List<String> userPaths;
}
