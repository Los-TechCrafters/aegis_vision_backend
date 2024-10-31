package techcrafters.aegisvision.alertservice.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import techcrafters.aegisvision.alertservice.domain.model.valueobject.AlertType;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String message;

  @Enumerated(EnumType.STRING)
  private AlertType type;

  @CreatedDate
  private Date createdDate;

}
