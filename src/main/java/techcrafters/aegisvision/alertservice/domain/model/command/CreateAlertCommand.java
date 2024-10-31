package techcrafters.aegisvision.alertservice.domain.model.command;

import techcrafters.aegisvision.alertservice.domain.model.valueobject.AlertType;

public record CreateAlertCommand(String message, AlertType alertType) {

}
