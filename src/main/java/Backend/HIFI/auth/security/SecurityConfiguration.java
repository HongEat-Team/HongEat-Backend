package Backend.HIFI.auth.security;

import Backend.HIFI.auth.jwt.JwtAuthenticationFilter;
import Backend.HIFI.auth.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/** 인증 및 Security 관련 설정 클래스입니다
 * @author gengminy (220728) */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                //예외처리 핸들러
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                //권한이 필요한 요청에 대한 설정
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/user/**").authenticated()
                .anyRequest().permitAll();

                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        // 검토 필요
    }

    /** cors 설정 configuration bean */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //로컬 react 개발 환경
        configuration.setAllowedOrigins(Arrays.asList(
                "https://hifihifi.site",
                "https://api.hifihifi.site",
                "http://localhost:3000",
                "http://localhost:3100"
        ));
        //서버 react 프론트 환경
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "TOKEN_ID", "X-Requested-With",
                "Authorization", "Content-Type",
                "Content-Length", "Cache-Control")
        );
        configuration.setAllowedMethods(Arrays.asList(
                "HEAD", "GET", "POST", "PUT", "DELETE", "OPTION"
        ));
        //내 서버의 응답 json 을 javascript에서 처리할수 있게 하는것(axios 등)
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /** 비밀번호 암호화 bcrypt Encoder 설정 */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
