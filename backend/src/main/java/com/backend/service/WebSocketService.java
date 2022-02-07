package com.backend.service;

import com.backend.entity.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
  @NonNull private final SimpMessagingTemplate simpMessagingTemplate;
  @NonNull private final IpService ipService;

  /**
   * Sends message to the user whose link has been followed by.
   * Message is sent by the websocket protocol.
   * @param link the link that has been followed
   */
  public void sendMessageToUser(Link link){
    simpMessagingTemplate
      .convertAndSendToUser(
        link.getUser().getEmail(), "/queue/followed-link", setUpMessage(link));
  }

  /**
   * Generates message for the given link to send by the websocket.
   * @param link Link object
   * @return WsMessage object
   */
  private WsMessage setUpMessage(Link link){
    return WsMessage
      .builder()
      .hash(link.getHash())
      .follows(link.getFollows())
      .uniqueFollows(ipService.countUniqueFollowsOfLink(link))
      .build();
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  private static class WsMessage{
    private String hash;
    private Long follows;
    private Long uniqueFollows;
  }
}