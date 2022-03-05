package cz.tix.memsourceinterview.api.dto;

import java.util.List;
import lombok.Data;

@Data
public abstract class MemsourceApiListEntity<T> {

  private List<T> content;
  private Integer totalElements;
  private Integer totalPages;
  private Integer pageNumber;
  private Integer numberOfElements;

}
