package techcrafters.aegisvision.alertservice.domain.service;

import techcrafters.aegisvision.alertservice.domain.model.command.CreateAlertCommand;
import techcrafters.aegisvision.alertservice.domain.model.command.DeleteAlertCommand;

public interface AlertCommandService {

  Long handle(CreateAlertCommand command);

  Long handle(DeleteAlertCommand command);
}
