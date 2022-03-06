package cz.tix.memsourceinterview.exception;

public class MemsourceApiException extends RuntimeException {

  public MemsourceApiException(String message) {
    super(message);
  }

  public MemsourceApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public MemsourceApiException(Throwable cause) {
    super(cause);
  }
}
