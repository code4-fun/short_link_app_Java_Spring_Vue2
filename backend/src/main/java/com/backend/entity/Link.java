package com.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="link")
public class Link {
  @Id
  private String hash;
  private String originUrl;
  private LocalDateTime expiration;
  private Long follows;
  @ManyToOne
  private User user;

  public void incrementFollows(){
    if(this.follows == null){
      this.follows = 1L;
    } else {
      this.follows++;
    }
  }
}