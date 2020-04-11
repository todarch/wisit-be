package com.todarch.wisitbe.explorer.flickr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wisit.explorer.flickr")
@Data
public class FlickrProperties {
  private String apiBaseUrl;
  private String apiKey;
}
