package cz.tix.memsourceinterview.dto;

import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class TokenDto {

  private String token;
  private ZonedDateTime expires;

}
