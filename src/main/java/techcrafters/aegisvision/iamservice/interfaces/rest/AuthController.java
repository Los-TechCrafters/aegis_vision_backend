package techcrafters.aegisvision.iamservice.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcrafters.aegisvision.iamservice.application.internal.queryservices.UserQueryServiceImpl;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateRefreshTokenCommand;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateTokenCommand;
import techcrafters.aegisvision.iamservice.domain.model.command.CreateUserCommand;
import techcrafters.aegisvision.iamservice.domain.model.entity.Token;
import techcrafters.aegisvision.iamservice.domain.model.entity.User;
import techcrafters.aegisvision.iamservice.domain.model.query.GetTokenByIdQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByEmailQuery;
import techcrafters.aegisvision.iamservice.domain.model.query.GetUserByIdQuery;
import techcrafters.aegisvision.iamservice.domain.service.TokenCommandService;
import techcrafters.aegisvision.iamservice.domain.service.TokenQueryService;
import techcrafters.aegisvision.iamservice.domain.service.UserCommandService;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.CreateUserResource;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.LoginResource;
import techcrafters.aegisvision.iamservice.interfaces.rest.resources.TokenResource;
import techcrafters.aegisvision.iamservice.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import techcrafters.aegisvision.iamservice.interfaces.rest.transform.TokenResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Management Endpoints")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenCommandService tokenCommandService;

  @Autowired
  private UserCommandService userCommandService;

  @Autowired
  private TokenQueryService tokenQueryService;

  @Autowired
  private UserQueryServiceImpl userQueryServiceImpl;

  @PostMapping("/register")
  public ResponseEntity<TokenResource> register(@RequestBody CreateUserResource resource) {
    CreateUserCommand createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
    Long userId = userCommandService.handle(createUserCommand);
    if(userId.equals(0L)){
      return ResponseEntity.badRequest().build();
    }
    GetUserByIdQuery getUserByIdQuery = new GetUserByIdQuery(userId);
    Optional<User> user = userQueryServiceImpl.handle(getUserByIdQuery);

    if(user.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    CreateTokenCommand createTokenCommand = new CreateTokenCommand(user.get());
    String tokenId = tokenCommandService.handle(createTokenCommand);

    return getTokenResourceResponseEntity(tokenId, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResource> login(@RequestBody LoginResource resource) {

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(resource.email(), resource.password()));

    GetUserByEmailQuery getUserByEmailQuery = new GetUserByEmailQuery(resource.email());
    Optional<User> user = userQueryServiceImpl.handle(getUserByEmailQuery);
    if(user.isEmpty()){
      return ResponseEntity.badRequest().build();
    }
    CreateTokenCommand createTokenCommand = new CreateTokenCommand(user.get());
    String tokenId = tokenCommandService.handle(createTokenCommand);
    return getTokenResourceResponseEntity(tokenId, null);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<TokenResource> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication){
    if (authentication == null || !authentication.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Invalid auth header");
    }
    String token = authentication.substring(7);
    String email = tokenQueryService.extractUsername(token);
    if(email.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    GetUserByEmailQuery getUserByEmailQuery = new GetUserByEmailQuery(email);
    Optional<User> user = userQueryServiceImpl.handle(getUserByEmailQuery);
    if(user.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    CreateRefreshTokenCommand createRefreshTokenCommand = new CreateRefreshTokenCommand(token, email, user.get());

    String refreshTokenId = tokenCommandService.handle(createRefreshTokenCommand);
    return getTokenResourceResponseEntity(refreshTokenId, null);
  }

  @NotNull
  private ResponseEntity<TokenResource> getTokenResourceResponseEntity(String tokenId, HttpStatus status) {

    if(tokenId.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    GetTokenByIdQuery getTokenByIdQuery = new GetTokenByIdQuery(tokenId);
    Optional<Token> refreshToken = tokenQueryService.handle(getTokenByIdQuery);

    if(refreshToken.isEmpty()){
      return ResponseEntity.badRequest().build();
    }

    TokenResource tokenResource = TokenResourceFromEntityAssembler.toResourceFromEntity(refreshToken.get());

    if(status != null){
      return ResponseEntity.status(status).body(tokenResource);
    }

    return ResponseEntity.ok(tokenResource);
  }
}
