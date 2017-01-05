package kz.codingwolves.uniquora.configurations;

import kz.codingwolves.uniquora.enums.Messages;
import kz.codingwolves.uniquora.models.User;
import kz.codingwolves.uniquora.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return (Authentication authentication) -> {
            //Checking if allright, otherwise throw exception
            User user = userRepository.findByEmail(authentication.getPrincipal().toString());
            if (user == null || !user.isRegistered()) {
                throw new AuthenticationServiceException(Messages.forbidden.toString());
            }
            if (!user.getPassword().equals(authentication.getCredentials().toString())) {
                logger.info("Bad credentials, email: " + user.getEmail());
                throw new AuthenticationServiceException(Messages.forbidden.toString());
            }
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), null);
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
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
            response.getWriter().write(Messages.success.toString());
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            response.setStatus(403);
            response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write(Messages.forbidden.toString());
        };
    }
}
