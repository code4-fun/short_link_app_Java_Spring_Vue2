package com.backend.controller;

import com.backend.controller.dto.LinkDto;
import com.backend.controller.response.ApiResponse;
import com.backend.controller.response.ApiResponseFactory;
import com.backend.entity.Link;
import com.backend.entity.User;
import com.backend.service.IpService;
import com.backend.service.LinkService;
import com.backend.service.UserService;
import javax.servlet.http.HttpServletRequest;
import liquibase.pro.packaged.T;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
  @NonNull private final UserService userService;
  @NonNull private final ApiResponseFactory apiResponseFactory;
  @NonNull private final LinkService linkService;
  @NonNull private final IpService ipService;

  @PostMapping("/links")
  public ApiResponse createLink(@AuthenticationPrincipal UserDetails principal,
                                @RequestBody LinkDto linkDto) throws Exception {
    User user = userService.findByEmail(principal.getUsername());
    return apiResponseFactory.success(linkService.generateLink(linkDto, user));
  }

  @GetMapping("/links")
  public ApiResponse getAllLinksOfUser(@AuthenticationPrincipal UserDetails principal){
    User user = userService.findByEmail(principal.getUsername());
    return user != null
      ? apiResponseFactory.success(linkService.getLinkDtosOfUser(user))
      : apiResponseFactory.failure();
  }

  @GetMapping("/{hash}")
  public ResponseEntity<T> redirect(@PathVariable("hash") String hash,
                                    HttpServletRequest request){
    Link link = linkService.incrementFollows(hash);
    ipService.saveUniqueIpForLink(link, request);
    return ResponseEntity
      .status(HttpStatus.SEE_OTHER)
      .header(HttpHeaders.LOCATION, link.getOriginUrl())
      .build();
  }

  @DeleteMapping("/links")
  public ApiResponse removeLink(@RequestParam("hash") String hash){
    linkService.deleteLink(hash);
    return apiResponseFactory.success();
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(Exception.class)
  public String return4xx(Exception ex) {
    return ex.getMessage();
  }
}