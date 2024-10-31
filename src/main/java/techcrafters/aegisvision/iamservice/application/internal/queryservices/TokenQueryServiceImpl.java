package techcrafters.aegisvision.iamservice.application.internal.queryservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.domain.model.query.GetTokenByIdQuery;
import techcrafters.aegisvision.iamservice.domain.service.TokenQueryService;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.TokenRepository;

@Service
public class TokenQueryServiceImpl implements TokenQueryService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Autowired
  private TokenRepository tokenRepository;

  @Override
  public Optional<Token> handle(GetTokenByIdQuery query) {
    return tokenRepository.findById(query.id());
  }


  @Override
  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
