package com.elnino.security.configure;

//import com.elnino.security.repository.UserRepository;
import com.elnino.security.domain.User;
import com.elnino.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/auth/token", "/auth/login", "/auth/introspect", "/auth/logout", "/auth/refresh"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

//    @Autowired
//    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                .decoder(customJwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // Bỏ tiền tố "SCOPE_" hoặc "ROLE_"
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); //Claim chứa roles trong JWT

//        Converter<Jwt, Collection<GrantedAuthority>> hybridConverter = jwt -> {
//            // Lấy roles từ JWT
//            Collection<GrantedAuthority> jwtAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
//
//            // Lấy roles từ Database (ví dụ: qua UserRepository)
//            String username = jwt.getSubject();
//            User user = userRepository.findUserByName(username);
//
//            // Chuyển đổi roles từ DB thành GrantedAuthority
//            Collection<GrantedAuthority> dbAuthorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Thêm tiền tố ROLE_
//                    .collect(Collectors.toList());
//
//            // Merge roles từ JWT và DB (loại bỏ trùng lặp)
//            Set<GrantedAuthority> combinedAuthorities = new HashSet<>();
//            if (jwtAuthorities != null) combinedAuthorities.addAll(jwtAuthorities);
//            combinedAuthorities.addAll(dbAuthorities);
//
//            return combinedAuthorities;
//        };

        // 3. Thiết lập converter tùy chỉnh vào JwtAuthenticationConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails user = User.builder()
//                .username("admin")
//                .password(encoder.encode("123456"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}