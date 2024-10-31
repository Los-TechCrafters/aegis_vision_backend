package techcrafters.aegisvision.iamservice.interfaces.rest.transform;

import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  public static UserResource toResourceFromEntity(User user) {
    return new UserResource(user.getName(), user.getEmail());
  }
}
