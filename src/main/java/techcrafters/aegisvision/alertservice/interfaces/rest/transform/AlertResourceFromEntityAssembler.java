package techcrafters.aegisvision.alertservice.interfaces.rest.transform;

import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;
import techcrafters.aegisvision.alertservice.interfaces.rest.resource.AlertResource;

public class AlertResourceFromEntityAssembler {

  public static AlertResource toResourceFromEntity(Alert alert) {
    return new AlertResource(alert.getId(), alert.getMessage(), alert.getType(), alert.getCreatedDate());
  }
}
