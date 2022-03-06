package cz.tix.memsourceinterview;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tix.memsourceinterview.api.dto.ListProjectDtoOut;
import cz.tix.memsourceinterview.dto.Project;
import cz.tix.memsourceinterview.dto.TokenDto;
import cz.tix.memsourceinterview.entity.MemsourceUser;
import cz.tix.memsourceinterview.repository.MemsourceUserRepository;
import java.time.ZonedDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test-application.properties")
@TestInstance(Lifecycle.PER_METHOD)
class MemsourceInterviewApplicationTests {

  public static final String API_PATH_URI_MEMSOURCE_PROJECTS = "/v1/memsource/projects";

  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MemsourceUserRepository memsourceUserRepository;

  @MockBean
  private RestTemplate restTemplate;
  @Value("${memsource.baseUri}")
  private String baseUri;

  private MemsourceUser testUserA;

  @BeforeEach
  public void init() {
    MemsourceUser testUser = new MemsourceUser();
    testUser.setUserName("user@test.cz");
    testUser.setPassword("password");
    this.testUserA = memsourceUserRepository.save(testUser);

    TokenDto tokenDto = getTestTokenDto();
    Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(TokenDto.class))).thenReturn(new ResponseEntity<>(tokenDto, HttpStatus.OK));

    ListProjectDtoOut listProjectDtoOut = getTestListProjectDtoOut();
    Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(ListProjectDtoOut.class))).thenReturn(new ResponseEntity<>(listProjectDtoOut, HttpStatus.OK));


  }

  @Test
  void hds_getProjectList() throws Exception {

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-userId", testUserA.getId())
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(2)));
  }

  @Test
  void hds_getProjectListMultipleTimes() throws Exception {

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-userId", testUserA.getId())
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(2)));

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-userId", testUserA.getId())
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(2)));
  }

  @Test
  void a1_noUserHeader() throws Exception {

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }


  @Test
  void a2_userNotFound() throws Exception {

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-userId", 999)

        )
        .andExpect(status().is4xxClientError())
        .andExpect(result -> assertEquals("Unable to find user with id 999", result.getResolvedException().getMessage()));
  }

  @Test
  void a3_remoteApiUnreachable() throws Exception {

    mvc.perform(
            get(API_PATH_URI_MEMSOURCE_PROJECTS)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }


  private TokenDto getTestTokenDto() {
    TokenDto tokenDto = new TokenDto();
    tokenDto.setToken("abcd");
    tokenDto.setExpires(ZonedDateTime.now().plusDays(1));
    return tokenDto;
  }

  private ListProjectDtoOut getTestListProjectDtoOut() {
    Project projectA = new Project();
    projectA.setName("A");
    projectA.setSourceLang("cz");
    projectA.setStatus("NEW");
    projectA.setTargetLangs(Arrays.asList("ab", "cd"));

    Project projectB = new Project();
    projectB.setName("B");
    projectB.setSourceLang("en");
    projectB.setStatus("COMPLETED");
    projectB.setTargetLangs(Arrays.asList("ab", "cd"));

    ListProjectDtoOut listProjectDtoOut = new ListProjectDtoOut();
    listProjectDtoOut.setContent(Arrays.asList(projectA, projectB));
    return listProjectDtoOut;
  }
}
