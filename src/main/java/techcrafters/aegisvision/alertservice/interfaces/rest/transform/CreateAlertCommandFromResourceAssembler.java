package techcrafters.aegisvision.alertservice.interfaces.rest.transform;

import techcrafters.aegisvision.alertservice.domain.model.command.CreateAlertCommand;
import techcrafters.aegisvision.alertservice.interfaces.rest.resource.CreateAlertResource;

public class CreateAlertCommandFromResourceAssembler {

  public static CreateAlertCommand toCommandFromResource(CreateAlertResource resource) {
    return new CreateAlertCommand(resource.message(), resource.alertType());
  }

}
