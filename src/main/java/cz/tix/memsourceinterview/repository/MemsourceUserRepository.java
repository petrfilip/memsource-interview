package cz.tix.memsourceinterview.repository;

import cz.tix.memsourceinterview.entity.MemsourceUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemsourceUserRepository extends JpaRepository<MemsourceUser, Long> {

  Optional<MemsourceUser> findMemsourceUserByUserName(String userName);
  Optional<MemsourceUser> findMemsourceUserById(Long userName);

}
