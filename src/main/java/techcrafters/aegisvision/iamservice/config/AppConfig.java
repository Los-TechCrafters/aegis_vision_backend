package techcrafters.aegisvision.iamservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.UserRepository;

@Configuration
public class AppConfig {

  @Autowired
  private UserRepository userRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      User user = userRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      return org.springframework.security.core.userdetails.User
          .builder()
          .username(user.getEmail())
          .password(user.getPassword())
          .build();
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}