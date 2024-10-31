package techcrafters.aegisvision.iamservice.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.model.query.GetAllUsersQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByEmailQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByIdQuery;
import techcrafters.aegisvision.iamservice.domain.service.UserQueryService;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class UserQueryServiceImpl implements UserQueryService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> handle(GetUserByIdQuery query) {
    return userRepository.findById(query.id());
  }

  @Override
  public Optional<User> handle(GetUserByEmailQuery query) {
    return userRepository.findByEmail(query.email());
  }

  @Override
  public List<User> handle(GetAllUsersQuery query) {
    return userRepository.findAll();
  }
}
