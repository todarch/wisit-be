package com.todarch.wisitbe.infrastructure.security;

import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Associates each request with a session using ip as an identifier.
 * Creates a new user if not registered on database yet.
 */
@Component
@AllArgsConstructor
@Slf4j
public class SecurityFilter extends GenericFilterBean {

  private final UserRepository userRepository;

  private final WisitEventPublisher wisitEventPublisher;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    CurrentUser currentUser = toCurrentUser(request);
    request.setAttribute(CurrentUser.class.getName(), currentUser);

    chain.doFilter(request, response);
  }

  private CurrentUser toCurrentUser(ServletRequest request) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String userIp = extractUserIp(httpServletRequest);
    User loadedUser = userRepository.findByIp(userIp)
        .orElseGet(() -> {
          User user = new User();
          user.setId(UUID.randomUUID().toString());
          user.setIp(userIp);
          user.setUserAgent(httpServletRequest.getHeader("User-Agent"));
          User newUser = userRepository.saveAndFlush(user);
          UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
          userCreatedEvent.setUserId(newUser.getId());
          wisitEventPublisher.publishEvent(userCreatedEvent);
          return newUser;
        });
    CurrentUser currentUser = new CurrentUser();
    currentUser.setId(loadedUser.getId());
    return currentUser;
  }

  /**
   * Extracts user ip from requests taking into account proxy.
   */
  private String extractUserIp(HttpServletRequest httpServletRequest) {
    String userIpFromProxyHeader = "x-forwarded-for";
    String userIpFromProxy = httpServletRequest.getHeader(userIpFromProxyHeader);
    if (userIpFromProxy == null) {
      return httpServletRequest.getRemoteAddr();
    }
    return userIpFromProxy;
  }
}
