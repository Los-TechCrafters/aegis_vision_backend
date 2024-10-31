package techcrafters.aegisvision.iamservice.domain.service;

import techcrafters.aegisvision.iamservice.domain.model.command.CreateRefreshTokenCommand;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateTokenCommand;

public interface TokenCommandService {

  String handle(CreateTokenCommand command);

  String handle(CreateRefreshTokenCommand command);

  Boolean isTokenValid(String token, String username, String email);
}
