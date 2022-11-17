package com.jabal.SpringBootImageAdminUbloadApp.config;


import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jabal.SpringBootImageAdminUbloadApp.service.UserDetailsServiceImpl;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());     
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
            .antMatchers("/upload/**").hasAnyAuthority("USER", "ADMIN")
            .antMatchers("/").permitAll()
            .antMatchers("/authenticateTheUser").permitAll()
            .antMatchers("/populateDropDownList/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/image/show/**").permitAll()
            .antMatchers("/register/**").permitAll()
            .antMatchers("/access-denied").permitAll()
//            .anyRequest().authenticated()
            .anyRequest().permitAll()
            .and()         
            .formLogin()
	            .loginPage("/login")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
            .and()
            .logout()
    		.invalidateHttpSession(true)
    		.clearAuthentication(true)
    		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    		.logoutSuccessUrl("/login?logout")
    		.permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/access-denied")       
//          .exceptionHandling().accessDeniedPage("/403")
            ;
    }
}







