package com.todarch.wisitbe.infrastructure.redis;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AllArgsConstructor
public class RedisConfig {

  private final RedisProperties redisProperties;

  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
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
