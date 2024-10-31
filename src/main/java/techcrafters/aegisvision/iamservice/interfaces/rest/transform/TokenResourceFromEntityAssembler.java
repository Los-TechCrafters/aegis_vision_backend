package techcrafters.aegisvision.iamservice.interfaces.rest.transform;

import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.TokenResource;

public class TokenResourceFromEntityAssembler {

  public static TokenResource toResourceFromEntity(Token token){
    return new TokenResource(token.getToken());
  }

}
