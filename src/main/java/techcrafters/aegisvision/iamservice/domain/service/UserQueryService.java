package techcrafters.aegisvision.iamservice.domain.service;

import java.util.List;
import java.util.Optional;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.model.query.GetAllUsersQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByEmailQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByIdQuery;

public interface UserQueryService {

  Optional<User> handle(GetUserByIdQuery query);

  Optional<User> handle(GetUserByEmailQuery query);

  List<User> handle(GetAllUsersQuery query);
}
