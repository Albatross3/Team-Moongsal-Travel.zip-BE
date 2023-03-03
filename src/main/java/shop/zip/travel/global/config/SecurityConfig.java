package shop.zip.travel.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest.H2ConsoleRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import shop.zip.travel.global.filter.JwtAuthenticationFilter;
import shop.zip.travel.global.filter.JwtExceptionFilter;
import shop.zip.travel.global.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(HttpMethod.OPTIONS,"/api/**")
        .requestMatchers("/api/auth/**")
        .requestMatchers("/docs/index.html")
        .requestMatchers("/api/healths")
        .requestMatchers(HttpMethod.GET,"/api/travelogues")
        .requestMatchers(HttpMethod.GET,"/api/travelogues/search");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .headers().frameOptions().disable()
        .and()
        .httpBasic().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests()
        .requestMatchers("h2-console/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
