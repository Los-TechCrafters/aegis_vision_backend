package techcrafters.aegisvision.iamservice.domain.service;

import java.util.Optional;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.domain.model.query.GetTokenByIdQuery;

public interface TokenQueryService {

  Optional<Token> handle(GetTokenByIdQuery query);

  String extractUsername(String token);
}
