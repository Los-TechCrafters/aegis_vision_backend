package techcrafters.aegisvision.alertservice.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

}
