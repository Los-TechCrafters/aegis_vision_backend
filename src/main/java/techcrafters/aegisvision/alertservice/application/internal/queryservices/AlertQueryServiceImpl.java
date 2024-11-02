package techcrafters.aegisvision.alertservice.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAlertByIdQuery;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAllAlertsQuery;
import techcrafters.aegisvision.alertservice.domain.service.AlertQueryService;
import techcrafters.aegisvision.alertservice.infrastructure.persistence.jpa.repositories.AlertRepository;

@Service
public class AlertQueryServiceImpl implements AlertQueryService {

  @Autowired
  private AlertRepository alertRepository;

  @Override
  public Optional<Alert> handle(GetAlertByIdQuery query) {
    return alertRepository.findById(query.id());
  }

  @Override
  public List<Alert> handle(GetAllAlertsQuery query) {
    return alertRepository.findAll();
  }
}
