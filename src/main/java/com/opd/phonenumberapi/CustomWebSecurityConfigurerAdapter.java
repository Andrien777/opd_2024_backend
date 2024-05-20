package com.opd.phonenumberapi;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

    DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
            new DelegatingJwtGrantedAuthoritiesConverter(
                    new JwtGrantedAuthoritiesConverter(),
                    new KeycloakJwtRolesConverter());

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK)).clearAuthentication(true));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(_jwt -> new JwtAuthenticationToken(_jwt, authoritiesConverter.convert(_jwt)))));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/getAllUsersData").hasAnyAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "USER",
                KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/searchUsers").hasAnyAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "USER",
                KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/addUser").hasAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/deleteUser").hasAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/editUser").hasAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/getUser").hasAnyAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "USER",
                KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/updatePhoneNumber").hasAnyAuthority(KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "USER",
                KeycloakJwtRolesConverter.PREFIX_REALM_ROLE + "ADMIN"));
        return http.build();
    }
}
