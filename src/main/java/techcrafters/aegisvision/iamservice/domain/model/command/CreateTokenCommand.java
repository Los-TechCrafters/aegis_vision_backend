package techcrafters.aegisvision.iamservice.domain.model.command;

import techcrafters.aegisvision.iamservice.domain.model.entity.User;

public record CreateTokenCommand(User user) {

}
