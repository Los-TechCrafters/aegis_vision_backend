package techcrafters.aegisvision.iamservice.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.TokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
      "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
      "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
      "/api/test/**", "/authenticate" };

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
            req.requestMatchers(WHITE_LIST_URL)
                .permitAll()
                .anyRequest()
                .authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout ->
            logout.logoutUrl("/auth/logout")
                .addLogoutHandler(this::logout)
                .logoutSuccessHandler((_, _, _) -> SecurityContextHolder.clearContext())
        )
    ;

    return http.build();
  }

  private void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication
  ) {

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    final String jwt = authHeader.substring(7);
    final Token storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);
    if (storedToken != null) {
      storedToken.setIsExpired(true);
      storedToken.setIsRevoked(true);
      tokenRepository.save(storedToken);
      SecurityContextHolder.clearContext();
    }
  }
}
