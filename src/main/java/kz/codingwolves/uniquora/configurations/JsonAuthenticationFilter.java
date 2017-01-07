package kz.codingwolves.uniquora.configurations;

import com.google.gson.Gson;
import kz.codingwolves.uniquora.dto.LoginDto;
import kz.codingwolves.uniquora.enums.Message;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by sagynysh on 12/23/16.
 */
//It is not used in the project after introducing JWT
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //Extracting principal and Credentials from request
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(Message.forbidden.toString());
        }
        LoginDto loginDto = new Gson().fromJson(jb.toString(), LoginDto.class);
        if (loginDto == null) {
            throw new AuthenticationServiceException(Message.forbidden.toString());
        }
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password));
    }
}
