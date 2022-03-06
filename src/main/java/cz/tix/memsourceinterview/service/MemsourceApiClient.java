package cz.tix.memsourceinterview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.tix.memsourceinterview.api.dto.ListProjectDtoOut;
import cz.tix.memsourceinterview.dto.TokenDto;
import cz.tix.memsourceinterview.dto.TokenRequestDto;
import cz.tix.memsourceinterview.entity.MemsourceUser;
import cz.tix.memsourceinterview.exception.MemsourceApiException;
import cz.tix.memsourceinterview.repository.MemsourceUserRepository;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
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
    log.info("Retrieving listProjects for userId {}", userId);
    return callApi(userId, HttpMethod.GET, "/web/api2/v1/projects", null, ListProjectDtoOut.class);
  }

  /**
   * Get token from cache or loaded from API
   *
   * @param userId user identification
   * @return token
   */
  public String retrieveToken(Long userId) {
    TokenDto cachedToken = tokenCache.get(userId);
    if (cachedToken != null && cachedToken.getExpires().isAfter(ZonedDateTime.now())) {
      log.debug("Token for user {} retrieved from cache ", userId);
      return cachedToken.getToken();
    }

    MemsourceUser memsourceUser = memsourceUserRepository.findMemsourceUserById(userId)
        .orElseThrow(() -> new IllegalStateException("Unable to find user"));

    TokenDto obtainedToken = obtainNewTokenFromApi(memsourceUser);
    tokenCache.put(userId, obtainedToken);
    return obtainedToken.getToken();
  }

  private TokenDto obtainNewTokenFromApi(MemsourceUser memsourceUser) {
    String authUri = baseUri + "/web/api2/v1/auth/login";

    TokenRequestDto tokenRequestDto = objectMapper.convertValue(memsourceUser, TokenRequestDto.class);
    ResponseEntity<TokenDto> tokenResponse = requestApiToken(authUri, tokenRequestDto);
    if (tokenResponse.getStatusCode().is2xxSuccessful() && isTokenResponseValid(tokenResponse.getBody())) {
      log.info("Obtained new token for user {} ", memsourceUser);
      return tokenResponse.getBody();
    } else {
      throw new MemsourceApiException("Unable to get token");
    }
  }

  private ResponseEntity<TokenDto> requestApiToken(String authUri, TokenRequestDto tokenRequestDto) {
    try {
      return restTemplate.postForEntity(authUri, tokenRequestDto, TokenDto.class);
    } catch (RestClientException e) {
      throw new MemsourceApiException("Unable to obtain token from API", e);
    }
  }

  private boolean isTokenResponseValid(TokenDto tokenDto) {
    return tokenDto != null
        && tokenDto.getToken() != null && !tokenDto.getToken().isEmpty()
        && tokenDto.getExpires() != null && tokenDto.getExpires().isAfter(ZonedDateTime.now());
  }

  private <T> T callApi(Long userId, HttpMethod httpMethod, String pathUri, Object dtoIn, Class<T> outputClass) {
    String targetUri = baseUri + pathUri;

    String token = retrieveToken(userId);

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "ApiToken " + token);

    HttpEntity<Object> requestEntity = new HttpEntity<>(dtoIn, headers);
    ResponseEntity<T> response = requestApi(httpMethod, outputClass, targetUri, requestEntity);
    log.debug("Request {} for {} has been executed", httpMethod, targetUri);
    if (response.getStatusCode().is2xxSuccessful()) {
      return response.getBody();
    } else {
      throw new MemsourceApiException("Request failed");
    }
  }

  private <T> ResponseEntity<T> requestApi(HttpMethod httpMethod, Class<T> outputClass, String targetUri, HttpEntity<Object> requestEntity) {
    try {
      return restTemplate.exchange(targetUri, httpMethod, requestEntity, outputClass);
    } catch (RestClientException e) {
      throw new MemsourceApiException(String.format("Unable to call target service %s", targetUri), e);
    }
  }


}
