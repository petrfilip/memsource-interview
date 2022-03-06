package cz.tix.memsourceinterview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemsourceApiException extends RuntimeException {

  public MemsourceApiException(String message) {
    super(message);
  }

  public MemsourceApiException(String message, Throwable cause) {
    super(message, cause);
  }

}
