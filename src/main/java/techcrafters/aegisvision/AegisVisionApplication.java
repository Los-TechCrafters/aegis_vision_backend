package techcrafters.aegisvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AegisVisionApplication {

  public static void main(String[] args) {
    SpringApplication.run(AegisVisionApplication.class, args);
  }

}
