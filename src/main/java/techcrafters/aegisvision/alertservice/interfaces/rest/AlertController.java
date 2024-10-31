package techcrafters.aegisvision.alertservice.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcrafters.aegisvision.alertservice.domain.model.command.CreateAlertCommand;
import techcrafters.aegisvision.alertservice.domain.model.command.DeleteAlertCommand;
import techcrafters.aegisvision.alertservice.domain.model.entity.Alert;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAlertByIdQuery;
import techcrafters.aegisvision.alertservice.domain.model.query.GetAllAlertsQuery;
import techcrafters.aegisvision.alertservice.domain.service.AlertCommandService;
import techcrafters.aegisvision.alertservice.domain.service.AlertQueryService;
import techcrafters.aegisvision.alertservice.interfaces.rest.resource.AlertResource;
import techcrafters.aegisvision.alertservice.interfaces.rest.resource.CreateAlertResource;
import techcrafters.aegisvision.alertservice.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import techcrafters.aegisvision.alertservice.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;

@RestController
@RequestMapping(value = "/api/v1/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Alerts Management Endpoints")
public class AlertController {

  @Autowired
  private AlertCommandService alertCommandService;

  @Autowired
  private AlertQueryService alertQueryService;

  @PostMapping
  public ResponseEntity<AlertResource> createAlert(@RequestBody CreateAlertResource resource){
    CreateAlertCommand createAlertCommand = CreateAlertCommandFromResourceAssembler.toCommandFromResource(resource);
    Long alertId = alertCommandService.handle(createAlertCommand);
    if(alertId.equals(0L)){
      return ResponseEntity.badRequest().build();
    }

    GetAlertByIdQuery getAlertByIdQuery = new GetAlertByIdQuery(alertId);
    Optional<Alert> alert = alertQueryService.handle(getAlertByIdQuery);
    if(alert.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    AlertResource alertResource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert.get());
    return new ResponseEntity<>(alertResource, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<AlertResource>> getAlerts(){
    GetAllAlertsQuery getAllAlertsQuery = new GetAllAlertsQuery();
    List<Alert> alerts = alertQueryService.handle(getAllAlertsQuery);
    if(alerts.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    List<AlertResource> alertResources = alerts.stream().map(AlertResourceFromEntityAssembler::toResourceFromEntity).toList();
    return ResponseEntity.ok(alertResources);
  }

  @GetMapping("/{alertId}")
  public ResponseEntity<AlertResource> getAlertById(@PathVariable Long alertId){
    GetAlertByIdQuery getAlertByIdQuery = new GetAlertByIdQuery(alertId);
    Optional<Alert> alert = alertQueryService.handle(getAlertByIdQuery);
    if(alert.isEmpty()){
      return ResponseEntity.badRequest().build();
    }
    AlertResource alertResource = AlertResourceFromEntityAssembler.toResourceFromEntity(alert.get());
    return ResponseEntity.ok(alertResource);
  }

  @DeleteMapping("/{alertId}")
  public ResponseEntity<Long> deleteAlert(@PathVariable Long alertId){
    DeleteAlertCommand deleteAlertCommand = new DeleteAlertCommand(alertId);
    Long deletedAlertId = alertCommandService.handle(deleteAlertCommand);
    if(deletedAlertId.equals(0L)){
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(deletedAlertId);
  }
}
