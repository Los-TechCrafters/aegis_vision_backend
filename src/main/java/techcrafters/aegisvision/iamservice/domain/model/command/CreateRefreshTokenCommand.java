package techcrafters.aegisvision.iamservice.domain.model.command;

import techcrafters.aegisvision.iamservice.domain.model.entity.User;

public record CreateRefreshTokenCommand(String refreshToken, String username, User user) {

}
