package techcrafters.aegisvision.iamservice.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.service.TokenCommandService;
import techcrafters.aegisvision.iamservice.domain.service.TokenQueryService;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.TokenRepository;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.UserRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenQueryService tokenQueryService;

  @Autowired
  private TokenCommandService tokenCommandService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = authHeader.substring(7);
    String email = tokenQueryService.extractUsername(jwt);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (email == null || authentication != null) {
      filterChain.doFilter(request, response);
      return;
    }

    final UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
    Boolean isTokenExpiredOrRevoked = tokenRepository.findByToken(jwt)
        .map(token -> !token.getIsExpired() && !token.getIsRevoked())
        .orElse(false);


    if (isTokenExpiredOrRevoked) {
      Optional<User> user = userRepository.findByEmail(email);

      if (user.isPresent()) {
        Boolean isTokenValid = tokenCommandService.isTokenValid(jwt, email, user.get().getEmail());

        if (isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          authToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }

    filterChain.doFilter(request, response);
  }

}
