package com.plantler.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.plantler.auth.oauth2.OAuth2SuccessHandler;
import com.plantler.auth.oauth2.OAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	// 로그인 요청 후 사용자 정보 부분
	private final OAuth2UserService oAuth2UserService;
	// 프론트쪽 리턴에 사용하는 Token 관련 부분
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    	http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.logout(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.cors(AbstractHttpConfigurer::disable);
		http.cors(cors -> cors.configurationSource(req -> corsOrigin()));
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(req -> {
			req.requestMatchers("/**").permitAll(); // 해당 URL 접근 허용
//			req.anyRequest().authenticated(); // 나머지 URL 접근 막기
		});
		http.addFilterBefore(jwtFilter
                , UsernamePasswordAuthenticationFilter.class);
		http.oauth2Login(oauth2 -> {
            oauth2.authorizationEndpoint(endpoint -> endpoint.baseUri("/api/login"));
            oauth2.redirectionEndpoint(endpoint -> endpoint.baseUri("/api/login/callback/*"));
            oauth2.userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService));
            oauth2.successHandler(oAuth2SuccessHandler);
        });
    	return http.build();
    }
    
    private CorsConfiguration corsOrigin() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);
        return corsConfig;
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return new BCryptPasswordEncoder();
    }

}
