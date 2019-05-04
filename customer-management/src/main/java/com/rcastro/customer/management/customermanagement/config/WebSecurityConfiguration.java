package com.rcastro.customer.management.customermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user/id/*").authenticated()
                .antMatchers(HttpMethod.GET, "/api/user").authenticated()
                .antMatchers(HttpMethod.POST, "/api/product/").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/product/").authenticated()
                .antMatchers("/api/user/wallet/*").permitAll()
                .antMatchers("/api/order").permitAll()
                .antMatchers("/api/activity").permitAll()
                .antMatchers(HttpMethod.GET, "/api/product").permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}