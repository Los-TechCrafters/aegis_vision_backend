package techcrafters.aegisvision.iamservice.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

  List<Token> findAllByUserAndIsExpiredIsFalseAndIsRevokedIsFalse(User user);

  Optional<Token> findByToken(String token);
}
