package techcrafters.aegisvision.iamservice.domain.service;

import techcrafters.aegisvision.iamservice.domain.model.command.CreateUserCommand;

public interface UserCommandService {

  Long handle(CreateUserCommand command);

}
