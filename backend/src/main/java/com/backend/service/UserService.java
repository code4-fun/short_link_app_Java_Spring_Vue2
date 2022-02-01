package com.backend.service;

import com.backend.entity.Role;
import com.backend.entity.User;
import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  @NonNull
  private final UserRepository userRepository;
  @NonNull
  private final RoleRepository roleRepository;
  @NonNull
  private final PasswordEncoder passwordEncoder;

  public User saveUser(User user) {
    Role role = roleRepository.findByName("ROLE_USER");
    user.setRole(role);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new EntityNotFoundException("There is no user with email " + email));
  }

  public User findByEmailAndPassword(String email, String password) {
    User user = findByEmail(email);
    if (user != null) {
      if (passwordEncoder.matches(password, user.getPassword())) {
        return user;
      }
    }
    return null;
  }
}