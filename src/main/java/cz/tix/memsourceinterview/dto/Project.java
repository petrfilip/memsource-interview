package cz.tix.memsourceinterview.dto;

import java.util.List;
import lombok.Data;

@Data
public class Project {

  private String name;
  private String status;
  private String sourceLang;
  private List<String> targetLangs;

}
