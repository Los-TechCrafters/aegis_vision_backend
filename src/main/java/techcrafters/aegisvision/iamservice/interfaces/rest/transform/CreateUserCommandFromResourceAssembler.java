package techcrafters.aegisvision.iamservice.interfaces.rest.transform;

import techcrafters.aegisvision.iamservice.domain.model.command.CreateUserCommand;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {

  public static CreateUserCommand toCommandFromResource(CreateUserResource resource) {
    return new CreateUserCommand(resource.name(), resource.email(), resource.password());
  }

}
