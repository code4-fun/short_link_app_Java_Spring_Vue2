package com.backend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
  private String hash;
  private String link;
  private String originUrl;
  private String dateOfExpiration;
  private String follows;
  private String uniqueFollows;
}