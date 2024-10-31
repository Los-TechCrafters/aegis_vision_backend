package techcrafters.aegisvision.alertservice.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.alertservice.domain.model.command.CreateAlertCommand;
import techcrafters.aegisvision.alertservice.domain.model.command.DeleteAlertCommand;
import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;
import techcrafters.aegisvision.alertservice.domain.service.AlertCommandService;
import techcrafters.aegisvision.alertservice.infrastructure.persistence.jpa.repositories.AlertRepository;

@Service
public class AlertCommandServiceImpl implements AlertCommandService {

  @Autowired
  private AlertRepository alertRepository;

  @Override
  public Long handle(CreateAlertCommand command) {
    Alert alert  = new Alert();
    alert.setMessage(command.message());
    alert.setType(command.alertType());
    alertRepository.save(alert);
    return alert.getId();
  }

  @Override
  public Long handle(DeleteAlertCommand command) {
    alertRepository.deleteById(command.id());
    return command.id();
  }
}
