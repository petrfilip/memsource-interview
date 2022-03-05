package cz.tix.memsourceinterview;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

  public static final String MEMSOURCE_API_PREFIX = "/v1/memsource";

  @Bean
  public RestTemplate createRestTemplate() {
    return new RestTemplate();
  }

}
