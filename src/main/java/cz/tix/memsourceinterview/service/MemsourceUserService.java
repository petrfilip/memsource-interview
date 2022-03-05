package cz.tix.memsourceinterview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tix.memsourceinterview.api.dto.CreateMemsourceUserDtoOut;
import cz.tix.memsourceinterview.entity.MemsourceUser;
import cz.tix.memsourceinterview.repository.MemsourceUserRepository;
import org.springframework.stereotype.Service;

@Service
public class MemsourceUserService {

  private final MemsourceUserRepository memsourceUserRepository;
  private final ObjectMapper objectMapper;

  public MemsourceUserService(MemsourceUserRepository memsourceUserRepository, ObjectMapper objectMapper) {
    this.memsourceUserRepository = memsourceUserRepository;
    this.objectMapper = objectMapper;
  }

  public CreateMemsourceUserDtoOut insertOrUpdate(String username, String password) {

    //todo validation

    MemsourceUser newUser = new MemsourceUser();
    newUser.setUserName(username);
    newUser.setPassword(password);
    MemsourceUser savedUser = memsourceUserRepository.save(newUser);

    return objectMapper.convertValue(savedUser, CreateMemsourceUserDtoOut.class);

  }

}
