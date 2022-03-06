package cz.tix.memsourceinterview.api;

import static cz.tix.memsourceinterview.Config.MEMSOURCE_API_PREFIX;

import cz.tix.memsourceinterview.api.dto.CreateMemsourceUserDtoIn;
import cz.tix.memsourceinterview.api.dto.CreateMemsourceUserDtoOut;
import cz.tix.memsourceinterview.service.MemsourceUserService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MEMSOURCE_API_PREFIX)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemsourceUserController {

  private final MemsourceUserService memsourceUserService;

  public MemsourceUserController(MemsourceUserService memsourceUserService) {
    this.memsourceUserService = memsourceUserService;
  }

  @PostMapping("users")
  public CreateMemsourceUserDtoOut createUser(@Valid @RequestBody CreateMemsourceUserDtoIn dtoIn) {
    return memsourceUserService.createNewUser(dtoIn.getUserName(), dtoIn.getPassword());
  }


}
