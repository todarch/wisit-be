package com.todarch.wisitbe.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wisit")
@Data
public class WisitProperties {
  private String internalHeaderName;
  private String internalHeaderValue;
}
