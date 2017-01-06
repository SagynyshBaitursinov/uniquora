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
@SuppressWarnings("ALL")
public class CorsAllowingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: There has to be a list of allowed origins
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (CorsUtils.isPreFlightRequest(request)) {
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            response.addHeader("Access-Control-Max-Age", "3600");
            response.getWriter().write(Messages.success.toString());
            response.setStatus(200);
            return;
        }
        doFilter(request, response, filterChain);
    }
}
