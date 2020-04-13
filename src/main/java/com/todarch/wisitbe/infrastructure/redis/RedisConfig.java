package com.todarch.wisitbe.infrastructure.redis;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AllArgsConstructor
public class RedisConfig {

  private final RedisProperties redisProperties;

  /**
   * Configures lettuce connection factory.
   */
  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
    standaloneConfiguration.setHostName(redisProperties.getHost());
    standaloneConfiguration.setPort(redisProperties.getPort());
    standaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
    return new LettuceConnectionFactory(standaloneConfiguration);
  }

  /**
   * Configures redis template.
   */
  @Bean
  public RedisTemplate<String, String> redisTemplate() {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    return template;
  }
}
