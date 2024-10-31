package techcrafters.aegisvision.iamservice.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateUserCommand;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.service.UserCommandService;
import techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class UserCommandServiceImpl implements UserCommandService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Long handle(CreateUserCommand command) {
    User user = new User();
    user.setEmail(command.email());
    user.setName(command.name());
    user.setPassword(passwordEncoder.encode(command.password()));
    userRepository.save(user);
    return user.getId();
  }
}
