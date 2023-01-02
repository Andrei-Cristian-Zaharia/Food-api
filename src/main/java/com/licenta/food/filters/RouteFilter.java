package com.licenta.food.filters;

import com.licenta.food.FoodConfigurations;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RouteFilter implements Filter {

    private static final String URI_PREFIX = "/v1/food-api";

    private static final String AUTH_HEADER = "authorities";

    private final FoodConfigurations foodConfigurations;

    public RouteFilter(FoodConfigurations foodConfigurations) {
        this.foodConfigurations = foodConfigurations;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String header = req.getHeader(AUTH_HEADER);

        if (("[ADMIN]").equals(req.getHeader(AUTH_HEADER))) {
            chain.doFilter(request, response);
            return;
        }

        if (("[GUEST]").equals(req.getHeader(AUTH_HEADER)) || ("[PERMIT]").equals(req.getHeader(AUTH_HEADER))) {
            chain.doFilter(request, response);
            return;
        }

        if (("[REGULAR_USER]").equals(req.getHeader(AUTH_HEADER))) {

            for (String route: foodConfigurations.getUserPaths()) {
                if (req.getRequestURI().equals(URI_PREFIX + route)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        res.setStatus(401);
        res.getOutputStream()
                .write(("Given authorities for request: " + req.getRequestURI() + " are not enough.").getBytes());
    }
}
