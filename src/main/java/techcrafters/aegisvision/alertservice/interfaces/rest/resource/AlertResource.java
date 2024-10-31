package techcrafters.aegisvision.alertservice.interfaces.rest.resource;

import java.util.Date;
import techcrafters.aegisvision.alertservice.domain.model.valueobject.AlertType;

public record AlertResource(Long id, String message, AlertType alertType, Date createdDate) {

}
