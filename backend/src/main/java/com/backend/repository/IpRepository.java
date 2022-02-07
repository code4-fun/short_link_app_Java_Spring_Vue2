package com.backend.repository;

import com.backend.entity.Ip;
import com.backend.entity.IpPk;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IpRepository extends CrudRepository<Ip, IpPk> {
  @Query(nativeQuery = true,
    value = "select count(*) from ip where ip.link_hash = :hash")
  Long countByLinkHash(@Param("hash") String hash);

  @Modifying(clearAutomatically=true, flushAutomatically=true)
  @Query(nativeQuery = true,
    value = "delete from ip where ip.link_hash = :hash")
  void deleteByHash(@Param("hash") String hash);
}