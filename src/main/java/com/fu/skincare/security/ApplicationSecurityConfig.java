package com.fu.skincare.security;

import com.fu.skincare.auth.ApplicationUserService;
import com.fu.skincare.jwt.JwtConfig;
import com.fu.skincare.jwt.TokenVerifier;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationUserService applicationUserService;
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new TokenVerifier(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/auth/register",
                        "/payments/call-back",
                        "/auth/login",
                        "/auth/**",
                        "/category/**",
                        "/product/**",
                        "/report/**",
                        "/question/**",
                        "/orderDetail/**",
                        "/userTest/**",
                        "/skinType/**",
                        "/blog/**",
                        "/bill/**",
                        "/account/**",
                        "/brand/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html/**",
                        "/v3/api-docs/",
                        "/swagger-ui/**",
                        "/recruitmentRequest/**",
                        "/employee/**",
                        "/interview/**")
                .permitAll()
                .anyRequest().authenticated();

    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(applicationUserService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public javax.crypto.SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }
        };
    }

}
