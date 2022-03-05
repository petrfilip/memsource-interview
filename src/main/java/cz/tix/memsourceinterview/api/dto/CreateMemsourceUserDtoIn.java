package cz.tix.memsourceinterview.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMemsourceUserDtoIn {

  @NotNull
  @Size(min=2, message="User name should have at least 2 characters")
  private String userName;

  @NotNull
  @Size(min=2, message="Password should have at least 7 characters")
  private String password;
}
