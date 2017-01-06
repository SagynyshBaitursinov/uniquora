package kz.codingwolves.uniquora.configurations;

import kz.codingwolves.uniquora.enums.Messages;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sagynysh on 1/6/17.
 */
public class CorsAllowingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        if (CorsUtils.isPreFlightRequest(request)) {
            response.getWriter().write(Messages.success.toString());
            response.setStatus(200);
            return;
        }
        doFilter(request, response, filterChain);
    }
}
