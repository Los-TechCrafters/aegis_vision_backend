package techcrafters.aegisvision.alertservice.domain.service;

import java.util.List;
import java.util.Optional;
import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAlertByIdQuery;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAllAlertsQuery;

public interface AlertQueryService {

  Optional<Alert> handle(GetAlertByIdQuery query);

  List<Alert> handle(GetAllAlertsQuery query);
}
