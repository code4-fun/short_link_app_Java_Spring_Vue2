package com.backend.repository;

import com.backend.entity.Link;
import com.backend.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, String> {
  List<Link> findAllByUser(User user);
  Optional<Link> findByHash(String hash);
  Long deleteByExpirationLessThanEqual(LocalDateTime dateTime);
}