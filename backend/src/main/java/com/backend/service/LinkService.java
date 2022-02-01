package com.backend.service;

import com.backend.controller.dto.LinkDto;
import com.backend.entity.Link;
import com.backend.entity.User;
import com.backend.repository.LinkRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {
  @NonNull private final LinkRepository linkRepository;
  @NonNull private final IpService ipService;

  @Value("${hostname}")
  private String hostname;
  @Value("${server.port}")
  private String port;

  /**
   * Returns list of LinkDtos of a particular user.
   * @param user user
   * @return list of LinkDto objects
   */
  public List<LinkDto> getLinkDtosOfUser(User user){
    return linkRepository
      .findAllByUser(user)
      .stream()
      .map(this::convertLinkToLinkDto)
      .collect(Collectors.toList());
  }

  /**
   * Converts Link object to LinkDto object.
   * @param link Link object
   * @return LinkDto object
   */
  public LinkDto convertLinkToLinkDto(Link link){
    return LinkDto
      .builder()
      .hash(link.getHash())
      .link(hostname + ":" + port + "/" + link.getHash())
      .originUrl(link.getOriginUrl())
      .dateOfExpiration(link.getExpiration() != null
        ? link.getExpiration().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        : null)
      .follows(link.getFollows() != null
        ? link.getFollows().toString()
        : null)
      .uniqueFollows(ipService.countUniqueFollowsOfLink(link) != null
        ? ipService.countUniqueFollowsOfLink(link).toString()
        : null)
      .build();
  }

  /**
   * Returns Link by hash.
   * @param hash hash
   * @return Link object
   */
  public Link findLinkByHash(String hash){
    return linkRepository
      .findByHash(hash)
      .orElseThrow(() -> new EntityNotFoundException("There is no hash " + hash));
  }

  /**
   * Deletes the Link entity with the given hash.
   * @param hash hash
   */
  public void deleteLink(String hash){
    findLinkByHash(hash);
    linkRepository.deleteById(hash);
  }

  /**
   * Increments by one the follows field of the Link object when
   * someone follows a link that corresponds to the given hash.
   * @param hash hash
   * @return Link object with incremented follows field
   */
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Link incrementFollows(String hash){
    Link link = findLinkByHash(hash);
    link.incrementFollows();
    return linkRepository.save(link);
  }


  /**
   * Generates random hash consisting of 8 chars[a-z,0-9] and saves the Link
   * based on this hash to the database.
   * @param linkDto LinkDto object containing information on the expiration
   *                date and origin URL from user   *
   * @param user user
   * @return LinkDto object constructed based on generated Link
   */
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public LinkDto generateLink(LinkDto linkDto, User user) throws Exception {
    if(linkDto.getOriginUrl() == null || linkDto.getOriginUrl().isEmpty()){
      throw(new Exception("Origin link must be provided"));
    }
    String charnum = "abcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder hash = new StringBuilder(8);
    Link link = null;

    do {
      for (int i = 0; i < 8; i++) {
        int random = new Random().nextInt(36);
        String _char = charnum.substring(random, random + 1);
        hash.append(_char);
      }

      link = saveLink(
        hash.toString(),
        convertStringToLocalDateTime(linkDto.getDateOfExpiration()),
        linkDto.getOriginUrl(),
        user);

    } while (link == null);
    return convertLinkToLinkDto(link);
  }

  /**
   * Creates Link object based on provided parameters
   * and saves it to the database or returns null if a link
   * with the given hash already exists in the database.
   * @param hash hash
   * @param expiration date of expiration. Can be null
   * @param originUrl origin URL
   * @param user user
   * @return Link object saved in database or null if a link
   * with the given hash already exists in the database
   */
  public Link saveLink(String hash, LocalDateTime expiration,
                         String originUrl, User user) {
    try {
      findLinkByHash(hash);
      return null;
    } catch (EntityNotFoundException e) {
      Link link = Link
        .builder()
        .hash(hash)
        .expiration(expiration)
        .originUrl(originUrl)
        .user(user)
        .build();
      return linkRepository.save(link);
    }
  }

  /**
   * Converts string with date to LocalDateTime object.
   * @param dateTime string with date
   * @return LocalDateTime object
   */
  private LocalDateTime convertStringToLocalDateTime(String dateTime){
    if(!dateTime.isEmpty()){
      int year = Integer.parseInt(dateTime.substring(0, 4));
      int month = Integer.parseInt(dateTime.substring(5, 7));
      int day = Integer.parseInt(dateTime.substring(8, 10));
      return LocalDateTime.of(year, month, day, 0, 0, 0, 0);
    }
    return null;
  }

  /**
   * Removes expired links every day at 00:00:00.
   */
  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void scheduledTasks() {
    linkRepository.deleteByExpirationLessThanEqual(LocalDateTime.now());
  }
}