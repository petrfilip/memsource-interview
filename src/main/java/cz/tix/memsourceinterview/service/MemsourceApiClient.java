package cz.tix.memsourceinterview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tix.memsourceinterview.api.dto.ListProjectDtoOut;
import cz.tix.memsourceinterview.dto.TokenDto;
import cz.tix.memsourceinterview.dto.TokenRequestDto;
import cz.tix.memsourceinterview.entity.MemsourceUser;
import cz.tix.memsourceinterview.repository.MemsourceUserRepository;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MemsourceApiClient {

  @Value("${memsource.baseUri}")
  private String baseUri;

  private final RestTemplate restTemplate;
  private final MemsourceUserRepository memsourceUserRepository;
  private final ObjectMapper objectMapper;

  private final Map<Long, TokenDto> tokenCache = new HashMap<>();

  public MemsourceApiClient(RestTemplate restTemplate, MemsourceUserRepository memsourceUserRepository, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.memsourceUserRepository = memsourceUserRepository;
    this.objectMapper = objectMapper;
  }

  public ListProjectDtoOut listProjects(Long userId) {
    return callApi(userId, HttpMethod.GET, "/web/api2/v1/projects", null, ListProjectDtoOut.class);
  }

  private String getToken(Long userId) {
    if (tokenCache.containsKey(userId)) {
      TokenDto token = tokenCache.get(userId);
      if (token.getExpires().isBefore(ZonedDateTime.now())) {
        return token.getToken();
      }
    }

    MemsourceUser memsourceUser = memsourceUserRepository.findMemsourceUserById(userId)
        .orElseThrow(() -> new IllegalStateException("Unable to find user"));

    TokenDto obtainedToken = obtainNewToken(memsourceUser);
    tokenCache.put(userId, obtainedToken);
    return obtainedToken.getToken();
  }

  private TokenDto obtainNewToken(MemsourceUser memsourceUser) {
    String authUri = baseUri + "/web/api2/v1/auth/login";

    TokenRequestDto tokenRequestDto = objectMapper.convertValue(memsourceUser, TokenRequestDto.class);
    ResponseEntity<TokenDto> tokenResponse = restTemplate.postForEntity(authUri, tokenRequestDto, TokenDto.class);
    if (tokenResponse.getStatusCode().is2xxSuccessful()) {
      return tokenResponse.getBody();
    } else {
      throw new IllegalStateException("Unable to get token");
    }
  }

  private <T> T callApi(Long userId, HttpMethod httpMethod, String uri, Object dtoIn, Class<T> outputClass) {
    String targetUri = baseUri + uri;

    String token = getToken(userId);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "ApiToken " + token);

    HttpEntity<Object> requestEntity = new HttpEntity<>(dtoIn, headers);
    ResponseEntity<T> response = restTemplate.exchange(targetUri, httpMethod, requestEntity, outputClass);
    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      throw new IllegalStateException("");
    }
  }


}
