package techcrafters.aegisvision.iamservice.application.internal.commandservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateRefreshTokenCommand;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateTokenCommand;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.model.valueobject.TokenType;
import techcrafters.aegisvision.iamservice.domain.service.TokenCommandService;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.TokenRepository;

@Service
public class TokenCommandServiceImpl implements TokenCommandService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private Long jwtExpiration;

  @Autowired
  private TokenRepository tokenRepository;

  @Override
  public String handle(CreateTokenCommand command) {
    return createToken(command.user());
  }

  @Override
  public String handle(CreateRefreshTokenCommand command) {
    Boolean isTokenValid = isTokenValid(command.refreshToken(), command.username(), command.user().getEmail());
    if(!isTokenValid){
      return "";
    }
    return createToken(command.user());
  }

  private String createToken(User user) {
    Token token = new Token();
    token.setUser(user);
    token.setToken(generateToken(user));
    token.setTokenType(TokenType.BEARER);
    token.setIsRevoked(false);
    token.setIsExpired(false);
    revokeAllUserTokens(user);
    tokenRepository.save(token);
    return token.getId();
  }

  public Boolean isTokenValid(String token, String username, String email) {
    return (username.equals(email)) && !isTokenExpired(token);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token){
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();
  }

  private void revokeAllUserTokens(User user){
    List<Token> validUserTokens = tokenRepository.findAllByUserAndIsExpiredIsFalseAndIsRevokedIsFalse(user);
    if(!validUserTokens.isEmpty()){
      validUserTokens.forEach(token -> {
        token.setIsRevoked(true);
        token.setIsExpired(true);
      });
      tokenRepository.saveAll(validUserTokens);
    }
  }

  private String generateToken(User user){
    return Jwts.builder()
        .claims(Map.of("name", user.getName()))
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey())
        .compact();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
