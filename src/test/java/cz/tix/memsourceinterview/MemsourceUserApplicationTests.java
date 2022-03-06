package cz.tix.memsourceinterview;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test-application.properties")
class MemsourceUserApplicationTests {

  public static final String API_PATH_URI_MEMSOURCE_USERS = "/v1/memsource/users";
  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  void hds_createNewMemsourceUser() throws Exception {

    Map<String, String> dtoIn = new HashMap<>(2);
    dtoIn.put("userName", "hello@world.cz");
    dtoIn.put("password", "pass");

    String dtoInAsString = objectMapper.writeValueAsString(dtoIn);

    mvc.perform(
            post(API_PATH_URI_MEMSOURCE_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoInAsString)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.userName", is("hello@world.cz")))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void a1_invalidDtoIn() throws Exception {

    Map<String, String> dtoIn = new HashMap<>(2);
    dtoIn.put("userName", "a");
    dtoIn.put("password", "b");

    String dtoInAsString = objectMapper.writeValueAsString(dtoIn);

    mvc.perform(
            post(API_PATH_URI_MEMSOURCE_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoInAsString)
        )
        .andExpect(status().is4xxClientError());
  }

}
