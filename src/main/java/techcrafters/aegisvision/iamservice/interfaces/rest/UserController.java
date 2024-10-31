package techcrafters.aegisvision.iamservice.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.model.query.GetAllUsersQuery;
import techcrafters.aegisvision.iamservice.domain.service.UserQueryService;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.UserResource;
import techcrafters.aegisvision.iamservice.interfaces.rest.transform.UserResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Users Management Endpoints")
public class UserController {

  @Autowired
  private UserQueryService userQueryService;

  @GetMapping
  public ResponseEntity<List<UserResource>> getUsers() {
    GetAllUsersQuery getAllUsersQuery = new GetAllUsersQuery();
    List<User> users = userQueryService.handle(getAllUsersQuery);
    if(users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    List<UserResource> userResources = users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
    return ResponseEntity.ok(userResources);
  }
}
