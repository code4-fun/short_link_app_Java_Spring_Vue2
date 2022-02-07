package com.backend.controller;

import com.backend.entity.User;
import com.backend.service.JwtUtil;
import com.backend.service.UserService;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class RegAuthController {
  @NonNull
  private final UserService userService;
  @NonNull
  private final JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(
      @RequestBody @Valid RegistrationRequest registrationRequest,
      UriComponentsBuilder uriComponentsBuilder) {
    User user = new User();
    user.setEmail(registrationRequest.getEmail());
    user.setFirstName(registrationRequest.getFirstName());
    user.setPassword(registrationRequest.getPassword());
    user.setLastName(registrationRequest.getLastName());
    userService.saveUser(user);
    return ResponseEntity
        .created(uriComponentsBuilder.path("/{email}").build(registrationRequest.getEmail()))
        .build();
  }

  @PostMapping("/auth")
  public AuthResponse auth(@RequestBody AuthRequest request) {
    User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
    String token = jwtUtil.generateToken(user.getEmail());
    return AuthResponse
      .builder()
      .token(token)
      .email(user.getEmail())
      .firstName(user.getFirstName())
      .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public String return400(Exception ex) {
    return ex.getMessage();
  }
}