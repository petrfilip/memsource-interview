package cz.tix.memsourceinterview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MemsourceUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "username")
  private String userName;

  @Column(name = "password")
  private String password;

  /**
   * Password omitted
   */
  @Override
  public String toString() {
    return "MemsourceUser{" +
        "id=" + id +
        ", userName='" + userName + '\'' +
        '}';
  }
}
