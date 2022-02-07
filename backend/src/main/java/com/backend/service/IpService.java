package com.backend.service;

import com.backend.entity.Ip;
import com.backend.entity.IpPk;
import com.backend.entity.Link;
import com.backend.repository.IpRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IpService {
  @NonNull private final IpRepository ipRepository;

  /**
   * Saves to the database Ip object when someone follows the Link.
   * Only unique IPs for each particular link are saved.
   * @param link Link object
   * @param request HttpServletRequest from which the IP is retrieved
   */
  public void saveUniqueIpForLink(Link link, HttpServletRequest request){
    if (request.getHeader("X-Forwarded-For") != null) {
      String xForwardedFor = request.getHeader("X-Forwarded-For");

      if (xForwardedFor.contains(",")) {
        Ip ip = buildIp(link, xForwardedFor.substring(xForwardedFor.lastIndexOf(",") + 2));
        ipRepository.save(ip);
      } else {
        Ip ip = buildIp(link, xForwardedFor);
        ipRepository.save(ip);
      }
    } else {
      Ip ip = buildIp(link, request.getRemoteAddr());
      ipRepository.save(ip);
    }
  }

  /**
   * Builds Ip object based on given Link and Ip.
   * @param link Link object
   * @param ip ip string
   * @return Ip object
   */
  private Ip buildIp(Link link, String ip){
    return Ip
      .builder()
      .ipPk(IpPk
        .builder()
        .link(link)
        .ip(ip)
        .build())
      .build();
  }

  /**
   * Returns number of unique follows by link.
   * @param link link
   * @return number of follows
   */
  public Long countUniqueFollowsOfLink(Link link){
    return ipRepository.countByLinkHash(link.getHash());
  }

  /**
   * Removes the Ip entities with the given hash.
   * @param hash hash
   */
  @Transactional
  public void deleteIpByHash(String hash){
    ipRepository.deleteByHash(hash);
  }
}