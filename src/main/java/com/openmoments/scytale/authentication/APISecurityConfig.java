package com.openmoments.scytale.authentication;

import com.openmoments.scytale.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String API_KEY_AUTH_HEADER = "X-API-Key";

    @Autowired
    private ClientRepository clientRepository;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIAuthFilter filter = new APIAuthFilter(API_KEY_AUTH_HEADER);
        filter.setAuthenticationManager(new APIAuthManager(clientRepository));

        httpSecurity.logout().disable();
        httpSecurity.formLogin().disable();
        httpSecurity.antMatcher("/api/v1/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(filter)
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
    }
}
