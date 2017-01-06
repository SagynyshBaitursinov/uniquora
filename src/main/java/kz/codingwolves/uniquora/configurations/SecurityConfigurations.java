package kz.codingwolves.uniquora.configurations;

import kz.codingwolves.uniquora.enums.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sagynysh on 12/21/16.
 */
@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonAuthenticationFilter jsonAuthenticationFilter;

    @Autowired
    private CorsAllowingFilter corsAllowingFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST,"/register").anonymous()
                .antMatchers(HttpMethod.GET,"/confirm").anonymous()
                .antMatchers(HttpMethod.GET, "/avatar/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint((HttpServletRequest request,
                                                                     HttpServletResponse response, AuthenticationException authException) -> {
                    response.setStatus(401);
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().write(Messages.login.toString());
                }).accessDeniedHandler((HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().write(Messages.forbidden.toString());
                })
                .and()
                .addFilterBefore(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsAllowingFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().write(Messages.success.toString());
                });
    }
}
