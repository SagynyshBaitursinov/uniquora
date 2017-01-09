package kz.codingwolves.uniquora.configurations;

import kz.codingwolves.jwt.JwtAuthenticationTokenFilter;
import kz.codingwolves.jwt.JwtTokenUtil;
import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Created by sagynysh on 12/27/16.
 */
@Configuration
public class SecurityBeansDefinitions {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return (Authentication authentication) -> {
            //Checking if allright, otherwise throw exception
            if (authentication.getPrincipal() == null) {
                throw new AuthenticationServiceException(Message.forbidden.toString());
            }
            User user = userRepository.findByEmail(authentication.getPrincipal().toString());
            if (user == null || !user.isRegistered() || user.getRemoved()) {
                throw new AuthenticationServiceException(Message.forbidden.toString());
            }
            if (!user.getPassword().equals(authentication.getCredentials())) {
                logger.info("Bad credentials, email: " + user.getEmail());
                throw new AuthenticationServiceException(Message.forbidden.toString());
            }
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), null);
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public CorsAllowingFilter corsAllowingFilter() {
        return new CorsAllowingFilter();
    }

/*  It was desided to use Jwt instead of sessions, so these are not needed
    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() {
        return new JsonAuthenticationFilter(customAuthenticationManager(), successHandler(), failureHandler());
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write(Message.success.toString());
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            response.setStatus(403);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write(Message.forbidden.toString());
        };
    }*/
}
