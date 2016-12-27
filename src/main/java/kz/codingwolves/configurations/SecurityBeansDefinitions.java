package kz.codingwolves.configurations;

import kz.codingwolves.enums.Messages;
import kz.codingwolves.models.User;
import kz.codingwolves.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by sagynysh on 12/27/16.
 */
@Configuration
public class SecurityBeansDefinitions {

    @Autowired
    UserRepository userRepository;

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return (Authentication authentication) -> {
            //Checking if allright, otherwise throw exception
            User user = userRepository.findByEmail(authentication.getPrincipal().toString());
            if (user == null || !user.getRegistered()) {
                throw new AuthenticationServiceException(Messages.forbidden.toString());
            }
            System.out.println(user.getFullname());
            if (!user.getPassword().equals(authentication.getCredentials().toString())) {
                throw new AuthenticationServiceException(Messages.forbidden.toString());
            }
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
        };
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() {
        return new JsonAuthenticationFilter(customAuthenticationManager(), successHandler(), failureHandler());
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.getWriter().write(Messages.success.toString());
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            response.setStatus(403);
            response.getWriter().write(Messages.forbidden.toString());
        };
    }
}
