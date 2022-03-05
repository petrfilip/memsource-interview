package cz.tix.memsourceinterview.api;

import static cz.tix.memsourceinterview.Config.MEMSOURCE_API_PREFIX;

import cz.tix.memsourceinterview.api.dto.ListProjectDtoOut;
import cz.tix.memsourceinterview.service.MemsourceApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MEMSOURCE_API_PREFIX)
public class ProjectController {

  @Autowired
  private MemsourceApiClient memsourceApiClient;

  @GetMapping("projects")
  public ListProjectDtoOut listProject(@RequestHeader("X-userId") Long userId) {
    return memsourceApiClient.listProjects(userId);
  }

}
