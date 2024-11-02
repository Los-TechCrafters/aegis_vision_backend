package techcrafters.aegisvision.alertservice.interfaces.rest.resource;

import techcrafters.aegisvision.alertservice.domain.model.valueobject.AlertType;

public record CreateAlertResource(String message, AlertType alertType) {

}
