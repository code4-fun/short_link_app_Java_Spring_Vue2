package com.backend.service;

import com.backend.entity.User;
import com.backend.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  @NonNull
  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new EntityNotFoundException("There is no user with email " + email));
    return CustomUserDetails.fromUserToCustomUserDetails(user);
  }
}